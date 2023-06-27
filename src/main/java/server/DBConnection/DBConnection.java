package server.DBConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
//        BufferedReader reader = new BufferedReader(new FileReader(System.getenv().get("DBCredetial")));
//        String[] credetials = reader.readLine().trim().split("=");
//        String url = credetials[0];
//        String user = credetials[1];
//        String password = credetials[2];
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs","s368162", "qXqc9g8kalBfMV5P");

    }
}
