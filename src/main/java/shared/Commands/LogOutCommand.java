package shared.Commands;

import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.commandsdto.CommandDTO;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public class LogOutCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private UserManager userManager;

    public LogOutCommand(CollectionManager collectionManager, UserManager userManager){
        super("logOut");
        this.collectionManager = collectionManager;
        this.userManager = userManager;
    }
    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        return userManager.logOut(requestBody);
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return super.executeRequest(null);
        }
        else{
            throw new WrongValuesException("В команде logOut не должно быть аргументов");
        }
    }

    @Override
    String getUsage() {
        return "logOut - команда для выхода из аккаунта";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
