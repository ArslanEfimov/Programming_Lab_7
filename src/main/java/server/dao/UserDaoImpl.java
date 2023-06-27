package server.dao;

import server.Queries;
import shared.auth.Credentials;
import shared.auth.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

public class UserDaoImpl implements DAO<User>{

    private final Connection connection;
    private static final int LOGIN_INDEX = 1;
    private static final int PASSWORD_INDEX = 2;
    private static final int SALT_INDEX = 3;
    private static Logger logger = Logger.getLogger(UserDaoImpl.class.getName());




    public UserDaoImpl(Connection connection){
        this.connection = connection;
    }
    @Override
    public Vector<User> read() {
        try {
            PreparedStatement preparedStatementUser = connection.prepareStatement(Queries.READ_ALL_DB_USER.getQuery());
            Vector<User> userVector = new Vector<>();
            ResultSet resultSetUser = preparedStatementUser.executeQuery();
            while (resultSetUser.next()) {
                int id = resultSetUser.getInt("id");
                String login = resultSetUser.getString("login");
                String password = resultSetUser.getString("password");
                userVector.add(new User(id, new Credentials(login, password)));
            }
            logger.info("Все пользователи были успешно загружены в коллекцию из базы данных!");
            return userVector;
        }catch (SQLException ex){
            return new Vector<>();
        }
    }
    public String getSalt(String login){
        try {
            PreparedStatement preparedStatementSalt = connection.prepareStatement(Queries.GET_SALT.getQuery());
            preparedStatementSalt.setString(1, login);
            ResultSet resultSetSalt = preparedStatementSalt.executeQuery();
            resultSetSalt.next();
            String salt = resultSetSalt.getString("salt");
            preparedStatementSalt.close();
            return salt;
        }catch (SQLException ex){
            return "";
        }

    }

    @Override
    public long create(User user) {
        try {
            PreparedStatement preparedStatementUser = connection.prepareStatement(Queries.CREATE_DB_USER.getQuery());
            preparedStatementUser.setString(LOGIN_INDEX, user.getCredentials().getLogin());
            preparedStatementUser.setString(PASSWORD_INDEX, user.getCredentials().getPassword());
            preparedStatementUser.setString(SALT_INDEX, user.getCredentials().getSalt());
            ResultSet resultSet = preparedStatementUser.executeQuery();
            resultSet.next();
            long id = resultSet.getLong("id");
            preparedStatementUser.close();
            return id;
        }catch (SQLException ex){
            logger.warning("Позьзователя не удалось добавить в базу данных!");
            return ERROR;
        }
    }
}
