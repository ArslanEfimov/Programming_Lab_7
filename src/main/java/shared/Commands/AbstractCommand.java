package shared.Commands;


import shared.core.Exceptions.WrongValuesException;
import shared.io.UserIO;
import shared.network.commandsdto.CommandDTO;
import shared.network.factory.RequestFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

abstract public class AbstractCommand {
    private final String commandName;
    private UserIO userIo = new UserIO();
    protected AbstractCommand(String commandName) {
        this.commandName = commandName;
    }
    abstract public Response executeResponse(RequestBody requestBody) throws SQLException;

    public Request executeRequest(String[] args) throws WrongValuesException {
        return RequestFactory.createRequest(getCommandName(), args);
    }

    abstract String getUsage();

    public String getCommandName(){
        return commandName;
    }
    public UserIO getUserIo(){
        return userIo;
    }

    abstract public boolean checkArguments(String[] args);

}
