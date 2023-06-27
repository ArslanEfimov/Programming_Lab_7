package server;
import server.DBConnection.DBConnection;
import server.ServerConnection.ServerConnection;
import server.dao.OrganizationDaoImpl;
import server.dao.TableDaoCreate;
import server.dao.UserDaoImpl;
import shared.auth.UserManager;
import shared.managers.AuthUserManager;
import shared.managers.CollectionManager;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerApplication {

    public static final int PORT = 1234;
    private OrganizationDaoImpl organizationDao;

    private UserDaoImpl userDao;
    private UserManager userManager;
    private CollectionManager collectionManager;
    private ServerConnection serverConnection;
    private boolean isStart = true;
    private static Logger logger = Logger.getLogger(ServerApplication.class.getName());

    public void run(){
//        FileManagerReader fileManagerReader = new FileManagerReader(fileName);
        DBConnection connection = new DBConnection();
        try {
            organizationDao = new OrganizationDaoImpl(connection.connect());
            userDao = new UserDaoImpl(connection.connect());
            userManager = new AuthUserManager(userDao);
            collectionManager = new CollectionManager(organizationDao, userManager);
            serverConnection = new ServerConnection(PORT, collectionManager, organizationDao, userManager);
        }catch (SQLException ex){
            logger.warning("Проблемы с подключением к базам данных! Сервер находится не в рабочем состоянии");
            isStart = false;
        } catch (ClassNotFoundException e) {
            logger.warning("Драйвер для подлкючения к базам данных не найден");
            isStart = false;
        }
        logger.info("Server connect!");
        System.out.println("---------- Доступные команды серверу: exit - вырубить сервер ----------");
        Thread consoleThread = new Thread(()->{
            Scanner commandServer = new Scanner(System.in);
            while(true){
                String commandLine = commandServer.nextLine();
                if("exit".equals(commandLine)){
                    logger.info("Завершение работы сервера!");
                    System.exit(0);
                    break;
                }
            }
        });
        consoleThread.start();
        try {
            if(isStart) {
                serverConnection.waitConnection();
            }
        }catch (IOException ex){
            logger.warning("Адрес занят!");
        }catch (ClassNotFoundException ex){
            logger.warning("Ошибка при сериализации данных");
        } catch (InterruptedException e) {
            logger.warning("Поток заблокирован!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
