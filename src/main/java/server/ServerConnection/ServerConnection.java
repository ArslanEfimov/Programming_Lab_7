package server.ServerConnection;
import server.dao.OrganizationDaoImpl;
import shared.auth.UserManager;
import shared.auth.UserState;
import shared.managers.CollectionManager;
import shared.managers.CommandManagers.CommandsManager;
import shared.network.Deserializers.Deserializer;
import shared.network.Serializers.Serializer;
import shared.network.messaging.Request;
import shared.network.messaging.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class ServerConnection {

    private final int port;
    private final InetSocketAddress address;
    private ByteBuffer byteBuffer;
    private final int BUFFER = 4096;
    private Selector selector;
    private CommandsManager commandsManager;
    private OrganizationDaoImpl organizationDao;
    private CollectionManager collectionManager;
    private UserManager userManager;
    private ExecutorService fixedThreadPoolRead = Executors.newFixedThreadPool(7);
    private ExecutorService fixedThreadPoolSend = Executors.newFixedThreadPool(5);
    private ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final Lock serializationLock = new ReentrantLock();
    private static Logger logger = Logger.getLogger(ServerConnection.class.getName());
    private byte[] bytes;
    private int bytesRead;
    private SocketChannel channel;

    public ServerConnection(int port, CollectionManager collectionManager, OrganizationDaoImpl organizationDao, UserManager userManager) throws SQLException {
        this.port = port;
        this.address = new InetSocketAddress(port);
        this.userManager = userManager;
        commandsManager = new CommandsManager(userManager);
        this.organizationDao = organizationDao;
        this.collectionManager = new CollectionManager(organizationDao, userManager);
    }

    public void waitConnection() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int readyChannel = selector.select();
                if (readyChannel == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                whichSelectedKey(iterator);
            }
        }catch (AlreadyBoundException ex){
            logger.warning("Соединение уже установлено" + ex);
        }catch (UnsupportedAddressTypeException ex){
            logger.warning("Адрес уже занят" + ex);
        }
    }
    private void whichSelectedKey(Iterator<SelectionKey> keyIterator) throws IOException, SQLException {
        while(keyIterator.hasNext()){
            SelectionKey key = keyIterator.next();
            keyIterator.remove();
            if(!key.isValid()){
                continue;
            }
            if(key.isAcceptable()){
                newConnection(key);
            }
            else if(key.isReadable()){
                readData(key);
            }
        }
    }

    private void newConnection(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        logger.info("Клиент " + channel.getRemoteAddress() +  " подключился!");
        if(channel == null){
            return;
        }
        channel.configureBlocking(false);
        key.attach(channel); // Привязка клиентского сокета к ключу
        channel.register(selector, SelectionKey.OP_READ); // Регистрация ключа в селекторе
        selector.wakeup();

    }


    private void readData(SelectionKey key) throws IOException, SQLException {
        fixedThreadPoolRead.execute(() -> {
                    channel = (SocketChannel) key.channel();
                    byteBuffer = ByteBuffer.allocate(BUFFER);
                    bytesRead = 0;
            try {
                bytesRead = channel.read(byteBuffer);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        }
        forkJoinPool.invoke(new RecursiveAction() {
                        @Override
                        protected void compute() {
                            if (bytesRead == -1) {
                                key.cancel();
                                try {
                                    channel.close();
                                } catch (IOException e) {
                                    logger.warning(e.getMessage());
                                }
                                logger.info("Клиент отключился");
                            } else {
                                bytes = new byte[bytesRead];
                                System.arraycopy(byteBuffer.array(), 0, bytes, 0, bytesRead);
                                Request request;
                                request = Deserializer.deserializeRequest(bytes);
                                Response response = new Response();
                                if (userManager.checkIfRegistered(request)) {
                                    commandsManager.fillServerMapDefault(collectionManager);
                                } else {
                                    commandsManager.fillServerMapAuth();
                                }
                                try {
                                    logger.info("Клиент " + channel.getRemoteAddress() + " отправил запрос: " + request.toString());
                                } catch (IOException e) {
                                    logger.warning(e.getMessage());
                                }
                                try {

                                    response = commandsManager.executeServer(request);
                                } catch (SQLException e) {
                                    logger.warning(e.getMessage());
                                }
                                if (userManager.checkIfRegistered(request) && response != null) {
                                    response.setUserState(UserState.REGISTERED);
                                } else if (response != null) {
                                    response.setUserState(UserState.NON_REGISTERED);
                                }
                                try {
                                    write(key, response);
                                } catch (IOException e) {
                                    logger.warning(e.getMessage());
                                }
                            }
                        }
                    });

    }

    private void write(SelectionKey key, Response response) throws IOException {
        fixedThreadPoolSend.execute(()-> {
            byte[] buffer;
            SocketChannel channel = (SocketChannel) key.channel();
            buffer = Serializer.serializeResponse(response);
            byteBuffer = ByteBuffer.wrap(buffer);
            while (byteBuffer.hasRemaining()) {
                try {
                    channel.write(byteBuffer);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        });
    }

}
