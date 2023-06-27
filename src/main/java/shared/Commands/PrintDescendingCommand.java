package shared.Commands;

import shared.core.Exceptions.WrongValuesException;
import shared.core.models.OrganizationCompareAnnualTurn;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.util.Vector;
import java.util.stream.Collectors;

public class PrintDescendingCommand extends AbstractCommand{

    private final CollectionManager collectionManager;
    public PrintDescendingCommand(CollectionManager collectionManager) {
        super("print_descending");
        this.collectionManager = collectionManager;
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return super.executeRequest(null);
        }
        else{
            throw new WrongValuesException("В команде info не должно быть аргументов, введите команду без аргументов");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) {
        if(collectionManager.getCollectionVector().size()!=0) {
            return ResponseFactory.createResponse(String.valueOf(collectionManager.getCollectionVector().stream().
                    sorted(new OrganizationCompareAnnualTurn()).collect(Collectors.toCollection(Vector::new))));
        }
        else{
            return ResponseFactory.createResponse("Коллекция пуста!");
        }
    }

    @Override
    String getUsage() {
        return "print_descending - возвращает все элементы в порядке убывания";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
