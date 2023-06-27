package shared.Commands;

import shared.SHA.SHA1PasswordRegistration;
import shared.auth.Credentials;
import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public class LoginCommand extends AbstractCommand{

    private final CollectionManager collectionManager;
    private UserManager userManager;

    public LoginCommand(CollectionManager collectionManager, UserManager userManager){
        super("login");
        this.collectionManager = collectionManager;
        this.userManager = userManager;

    }
    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        return userManager.login(requestBody);
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            Request request = super.executeRequest(args);
            RequestBody requestBody = request.getRequestBody();
            requestBody.setCredentials(new Credentials(getUserIo().registerUserLogin(), SHA1PasswordRegistration.hashPasswordForLoginWithPepper(getUserIo().registerUserPassword())));
            return request;
        }
        throw new WrongValuesException("У команды login нет аргументов");
    }

    @Override
    String getUsage() {
        return "login - команда для входа авторизированного пользователя в аккаунт";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
