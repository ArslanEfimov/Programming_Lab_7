package shared.Commands;

import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public class ClearCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public ClearCommand(CollectionManager collectionManager) {
        super("clear");
        this.collectionManager = collectionManager;
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if (checkArguments(args)) {
            return super.executeRequest(null);
        }
        else{
            throw new WrongValuesException("В команде clear не должно быть аргументов, введите команду без аргументов");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        return collectionManager.clearUserCollection(requestBody);
    }

    @Override
    String getUsage() {
        return "clear - команда очищает коллекцию";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
