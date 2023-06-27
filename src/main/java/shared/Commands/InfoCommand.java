package shared.Commands;


import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

public class InfoCommand extends AbstractCommand{

    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response executeResponse(RequestBody requestBody){
        return new Response(collectionManager.toString());
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)) {
            return super.executeRequest(null);
        }
        else{
            throw new WrongValuesException("В команде info не должно быть аргументов, введите команду без аргументов");
        }
    }

    @Override
    String getUsage() {
        return "info - выводит информацию о коллекции";
    }

    @Override
    public boolean checkArguments(String[] args){
        return args.length==0;
    }
}
