package shared.Commands;


import shared.auth.UserManager;
import shared.core.Exceptions.InvalidExecuteRequestException;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public class RemoveByIdCommand extends AbstractCommand{
    
    private final CollectionManager collectionManager;
    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id");
        this.collectionManager = collectionManager;
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return super.executeRequest(args);
        }
        else{
            throw new WrongValuesException("В команде remove_by_id должны быть аргументы типа (Long) и большие 0");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        if(collectionManager.getCollectionVector().size()!=0) {
            Long id = Long.parseLong(requestBody.getArgs()[0]);
            return collectionManager.removeById(requestBody,id);
        }
        else{
            return ResponseFactory.createResponse("Коллекция пуста!");
        }
    }

    @Override
    String getUsage() {
        return "remove_by_id - удаляет элемент из коллекции по его id";
    }

    @Override
    public boolean checkArguments(String[] args) {
        if(args.length==0) {
            return false;
        }
        else{
            try{
                if(Long.parseLong(args[0])<=0){
                    return false;
                }
                Long.parseLong(args[0]);
                return true;
            }catch (NumberFormatException ex){
                return false;
            }
            
        }
    }
}
