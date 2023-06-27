package client.ClientConnection;

import client.ClientMain;

import java.io.IOException;
import java.sql.SQLException;

public class ClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        ClientMain clientMain = new ClientMain();
        clientMain.run();
    }
}
