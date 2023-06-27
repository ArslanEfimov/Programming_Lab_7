package shared.Commands;

import shared.auth.User;
import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.io.UserIO;
import shared.managers.CollectionManager;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.util.LinkedList;

public class UpdateCommand extends AbstractCommand{

    private final CollectionManager collectionManager;
    private UserIO userIO;
    private LinkedList<String> update;
    private UserManager userManager;
    public UpdateCommand(CollectionManager collectionManager, UserManager userManager){
        super("update_id");
        this.collectionManager = collectionManager;
        this.userIO = new UserIO();
        this.update = new LinkedList<>();
        this.userManager = userManager;
    }


    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)) {
            return super.executeRequest(args);
        }
        else{
            throw new WrongValuesException("В команде update_id должны быть аргументы типа (int)");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) {
        Long id = Long.parseLong(requestBody.getArgs()[0]);
        User user = userManager.findUser(requestBody.getCredentials());
        if(collectionManager.getCollectionVector().stream().filter(el->el.getId()==id).filter(org -> org.getUserId()== user.getId()).count()==1){
            update.addLast("getId " + id);
            UserIO.setCommandsToEnter(update);
            return null;
        }
        else{
            return ResponseFactory.createResponse("Такого элемента нет в коллекции или данный элемент принадлежит не вам!!");
        }
    }

    @Override
    String getUsage() {
        return "update_id - обновляет элемент коллекции по заданному id";
    }

    @Override
    public boolean checkArguments(String[] args) {
        if(args.length==0){
            return false;
        }
        else{
            try {
                Integer.parseInt(args[0].trim());
                return true;
            }catch (NumberFormatException ex){
                return false;
            }
        }
    }
}
