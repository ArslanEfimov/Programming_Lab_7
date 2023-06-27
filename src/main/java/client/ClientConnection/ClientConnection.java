package client.ClientConnection;

import shared.network.Deserializers.Deserializer;
import shared.network.Serializers.Serializer;
import shared.network.messaging.Request;
import shared.network.messaging.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection{

    private int port;
    private InetSocketAddress address;
    private Socket socket;
    private final int BUFFER = 4096;
    private byte[] buffer;


    public ClientConnection(String hostAddress, int port) throws IOException {
        this.port = port;
        this.address = new InetSocketAddress(hostAddress, port);
        this.socket = new Socket(address.getHostName(), address.getPort());
        this.buffer = new byte[BUFFER];
    }

    public ClientConnection(){
        this.address = new InetSocketAddress(port);
    }


    public Response getDataFromServer() throws IOException {
        InputStream inputStream = new BufferedInputStream(socket.getInputStream());
        int bytesRead = inputStream.read(buffer);
        if(bytesRead==-1) throw new SocketException();
        byte[] bytes = new byte[bytesRead];
        System.arraycopy(buffer, 0, bytes, 0, bytesRead);
        return Deserializer.deserializeResponse(bytes);
    }

    public void sendDataToServer(Request request)  {
        try {
            OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
            outputStream.write(Serializer.serializeRequest(request));
            outputStream.flush();
        }catch (IOException ex){
            System.out.println("Ошибка IO");
        }
    }

    public void tryReconnect(){
        int retryDelay = 15000; // Время ожидания перед первой попыткой (в миллисекундах)
        boolean connected = false;
        int counter = 0;
        while (!connected) {
            try {
                socket = new Socket(address.getHostName(), address.getPort());
                connected = true;

            } catch (IOException ex) {
                if(counter>=1){
                    System.err.println("Превышено время ожидания ответа!");
                    System.exit(1);
                }
                System.err.println("Ошибка подключения! Пробуем подключиться, пожалуйста, ожидайте ответа");
                counter+=1;
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException e) {
                    System.err.println("Ошибка при приостановке потока!");
                }

            }
        }
    }


}
