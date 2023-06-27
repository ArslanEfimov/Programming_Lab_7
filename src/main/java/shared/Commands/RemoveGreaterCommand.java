package shared.Commands;

import shared.core.Exceptions.InvalidExecuteRequestException;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public class RemoveGreaterCommand extends AbstractCommand{

    private CollectionManager collectionManager;
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater");
        this.collectionManager = collectionManager;
    }
    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return super.executeRequest(args);
        }
        else{
            throw new WrongValuesException("В команде remove_greater должны быть аргументы типа (Float) большие 0");
        }
    }

    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        if(collectionManager.getCollectionVector().size()!=0) {
            Float annualTurnover = Float.parseFloat(requestBody.getArgs()[0].trim().replace(",", "."));
            return collectionManager.removeGreater(requestBody, annualTurnover);
        }else{
            return ResponseFactory.createResponse("Коллекция пуста!");
        }
    }

    @Override
    String getUsage() {
        return "remove_greater - удаляет элемент из коллекции, чье значение равно вводимому значению annualturnover";
    }

    @Override
    public boolean checkArguments(String[] args) {
        if(args.length == 0) {
            return false;
        }
        else{
            try{
                Float annualTurnover = Float.parseFloat(args[0].trim().replace(",","."));
                if(annualTurnover<=0){
                    return false;
                }
                return true;
            }catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
