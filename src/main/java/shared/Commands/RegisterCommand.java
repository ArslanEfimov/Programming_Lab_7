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

public class RegisterCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private UserManager userManager;

    public RegisterCommand(CollectionManager collectionManager, UserManager userManager){
        super("register");
        this.collectionManager = collectionManager;
        this.userManager = userManager;
    }
    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        return userManager.register(requestBody);
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            Request request = super.executeRequest(args);
            RequestBody requestBody = request.getRequestBody();
            requestBody.setCredentials(new Credentials(getUserIo().registerUserLogin(), SHA1PasswordRegistration.hashPassword(getUserIo().registerUserPassword()), SHA1PasswordRegistration.getSalt()));
            return request;
        }
        throw new WrongValuesException("У команды register не должно быть аргументов!");
    }

    @Override
    String getUsage() {
        return "register - команда для регистрации новых пользователей";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
