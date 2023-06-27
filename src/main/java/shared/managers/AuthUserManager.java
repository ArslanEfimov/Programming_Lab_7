package shared.managers;

import server.dao.DAO;
import server.dao.UserDaoImpl;
import shared.SHA.SHA1PasswordRegistration;
import shared.auth.Credentials;
import shared.auth.User;
import shared.auth.UserManager;
import shared.auth.UserState;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;
import java.util.logging.Logger;

public class AuthUserManager implements UserManager, Serializable {

    private Vector<User> userVector;
    private final UserDaoImpl userDao;
    private static Logger logger = Logger.getLogger(AuthUserManager.class.getName());
    public AuthUserManager(UserDaoImpl userDao){
        this.userDao = userDao;
        this.userVector = userDao.read();
    }
    @Override
    public Response register(RequestBody request){
        Credentials credentials = request.getCredentials();
        for(User users : userVector){
            if(users.getCredentials().getLogin().equals(credentials.getLogin())){
                return ResponseFactory.createResponse("Пользователь с таким логином уже существует, используйте другой логин!");
            }
        }
        User user = new User(credentials);
        int id = (int) userDao.create(user);
        if(id == DAO.ERROR){
            return ResponseFactory.createResponse("Ошибка при регистрации пользователя!");
        }
        user.setId(id);
        userVector.add(user);
        Response response = new Response();
        logger.info("Пользователь " + user.getCredentials().getLogin() + " зарегестрирован");
        response.setServerMessage("Вы успешно зарегестрированы!");
        response.setUserState(UserState.REGISTERED);
        return response;
    }

    @Override
    public Response login(RequestBody requestBody){
        Credentials credentials = requestBody.getCredentials();
        String pwd = SHA1PasswordRegistration.hashPasswordForLogin(credentials.getPassword(), userDao.getSalt(credentials.getLogin()));
        String login = credentials.getLogin();
        boolean hasUser = false;
        for(User users : userVector){
            if(users.getCredentials().getLogin().equals(login) && users.getCredentials().getPassword().equals(pwd)){
                hasUser = true;
            }
        }
        if(hasUser==true){
            Response response = new Response();
            response.setUserState(UserState.REGISTERED);
            logger.info("Пользователь под логином " + login + " зашел в свой аккаунт");
            return ResponseFactory.createResponse(login + " успешно вошел в аккаунт");

        }
        else{
            return ResponseFactory.createResponse("Пароль или логин введены неверно, попробуйте еще раз");
        }
    }

    @Override
    public Response logOut(RequestBody requestBody) {
        Response response = new Response();
        response.setUserState(UserState.NON_REGISTERED);
        if(requestBody.getCredentials()!=null) {
            logger.info("Пользователь под логином " + requestBody.getCredentials().getLogin() + " вышел из аккаунта");
            requestBody.setCredentials(null);
            return ResponseFactory.createResponse("До свидания!");
        }
        else{
            return ResponseFactory.createResponse("Вы еще не авторизировались, поэтому данную команду не можете выполнить");
        }
    }

    @Override
    public boolean checkIfRegistered(Request request) {
        if(request.getCredentials()==null) {
            return false;
        }
        String login = request.getCredentials().getLogin();
        for(User users : userVector){
            if(users.getCredentials().getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    @Override
    public User findUser(Credentials credentials) {
        Optional<User> user = userVector.stream().filter(userCr -> userCr.getCredentials().getLogin().equals(credentials.getLogin())).findFirst();
        return user.orElse(null);
    }

}
