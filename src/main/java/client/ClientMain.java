package client;
import client.ClientConnection.ClientConnection;
import shared.auth.Credentials;
import shared.auth.UserState;
import shared.io.UserIO;
import shared.managers.CommandManagers.CommandsManager;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.LinkedList;

public class ClientMain {

    private static final int PORT = 1234;
    private static final String HOST_NAME = "localhost";
    private Credentials credentials = null;
    public void run() throws InterruptedException, IOException{
        UserIO userIO = new UserIO();
        userIO.println("~~~~~~~~~Вы запустили наше приложение, войдите в свой аккаунт или зарегестрируйтесь чтобы ознакомиться с командами, введите команду help~~~~~~~~~~");
        CommandsManager commandsManager = new CommandsManager();
        ClientConnection clientConnection = null;
        commandsManager.fillClientMapAuth();
        while (userIO.getClientWork()) {
            if(clientConnection==null) {
                int retryDelay = 10000;
                boolean connected = false;
                int counter = 0;
                while(!connected) {
                    try {
                        clientConnection = new ClientConnection(HOST_NAME, PORT);
                        connected = true;
                    } catch (IOException ex) {
                        if(counter>=1){
                            System.err.println("Превышено время ожидания подключения!");
                            System.exit(1);
                        }
                        userIO.printerr("Клиент не может подключится к серверу! Пытаемся установить содениение....");
                        counter+=1;
                        Thread.sleep(retryDelay);
                    }
                }

            }
                userIO.print("\nВведите команду: ");
                userIO.printPreamble();
                String str = userIO.readLine().trim();
                UserIO.getCommandsToEnter().addLast(str);
                while(UserIO.getCommandsToEnter().size()!=0) {
                    String st = UserIO.getCommandsToEnter().poll();
                    if (commandsManager.commandCheck(st.split(" ")[0])) {
                        try {
                            Request request = commandsManager.executeClient(st);
                            if (request == null) {
                                continue;
                            }
                            if (request.getRequestBody().getCredentials() == null){
                                request.setCredentials(this.credentials);
                                request.getRequestBody().setCredentials(this.credentials);
                                }
                            else{
                                this.credentials = request.getRequestBody().getCredentials();
                                request.setCredentials(this.credentials);
                            }
                            if(request.getCommand().getName().toString().equals("logOut")){
                                this.credentials = null;
                                request.setCredentials(this.credentials);
                            }
                            clientConnection.sendDataToServer(request);
                            Response response = clientConnection.getDataFromServer();
                            if (response == null) {
                                LinkedList<String> update = new LinkedList<>();
                                update.add("getId " + str.split("\\s+")[1]);
                                UserIO.setCommandsToEnter(update);
                                continue;
                            }
                            if(response.getUserState()== UserState.REGISTERED){
                                commandsManager.fillClientMapDefault();
                            }
                            else{
                                commandsManager.fillClientMapAuth();
                            }
                            userIO.println(String.valueOf(response));
                        } catch (SocketException ex) {
                            clientConnection.tryReconnect();
                            Request request = commandsManager.executeClient(st);
                            if (request == null) {
                                continue;
                            }
                            clientConnection.sendDataToServer(request);
                            Response response = clientConnection.getDataFromServer();
                            if (response == null) {
                                LinkedList<String> update = new LinkedList<>();
                                update.add("getId " + str.split("\\s+")[1]);
                                UserIO.setCommandsToEnter(update);
                                continue;
                            }
                            userIO.println(String.valueOf(response));
                        }
                    }
                }
        }
    }
}
