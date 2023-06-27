package shared.Commands;

import shared.auth.Credentials;
import shared.auth.UserState;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

public class ShowCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager) {
        super("show");
        this.collectionManager = collectionManager;
    }


    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            Request request = super.executeRequest(args);
            return request;
        }
        else{
            throw new WrongValuesException("В команде show не должно быть аргументов, введите команду без аргументов");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) {
        return ResponseFactory.createResponse(collectionManager.getCollectionVector().toString());
    }

    @Override
    String getUsage() {
        return "show - команда выводит все элементы в коллекции";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
