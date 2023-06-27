package server.dao;

import server.Queries;
import server.ServerConnection.ServerConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class TableDaoCreate {

    private final Connection connection;
    private static Logger logger = Logger.getLogger(TableDaoCreate.class.getName());

    public TableDaoCreate(Connection connection) throws SQLException {
        this.connection = connection;
        createOrganizationTable();
        createCoordinatesTable();
        createUserTable();
        createAddressTable();
    }

    private void createUserTable() throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute(Queries.CREATE_DB_USER.getQuery());

    }

    private void createCoordinatesTable() throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute(Queries.CREATE_DB_COORDINATES.getQuery());
    }
    private void createAddressTable() throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute(Queries.CREATE_DB_ADDRESS.getQuery());
    }
    private void createOrganizationTable() throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute(Queries.CREATE_DB_ORGANIZATION.getQuery());
    }
}
