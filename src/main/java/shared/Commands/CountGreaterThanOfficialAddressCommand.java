package shared.Commands;


import shared.core.Exceptions.WrongValuesException;
import shared.core.models.Address;
import shared.io.UserIO;
import shared.managers.CollectionManager;
import shared.network.factory.RequestFactory;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.RequestBodyAddress;
import shared.network.messaging.Response;

public class CountGreaterThanOfficialAddressCommand extends AbstractCommand{

    private final CollectionManager collectionManager;
    private UserIO userIO;
    public CountGreaterThanOfficialAddressCommand(CollectionManager collectionManager) {
        super("count_greater_than_official_address");
        this.collectionManager = collectionManager;
        this.userIO = new UserIO();
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return RequestFactory.createRequestAddress(getCommandName(), args, new Address(args[0]));
        }
        else{
            throw new WrongValuesException("В команде count_greater_than_official_address должны быть аргументов и длина адреса должна быть больше 0, но меньше 130");
        }
    }
    @Override
    public Response executeResponse(RequestBody requestBody) {
        if(collectionManager.getCollectionVector().size()!=0) {
            Address officialAddress = ((RequestBodyAddress) requestBody).getAddress();
            int count = collectionManager.countGreaterThanOfficicalAddress(officialAddress);
            return ResponseFactory.createResponse("Кол-во элементов: " + (count));
        }else{
            return ResponseFactory.createResponse("Коллекция пуста!");
        }
    }


    @Override
    String getUsage() {
        return "count_greater_than_official_address - выводит количество элементов, чье значение адреса больше заданного";
    }

    @Override
    public boolean checkArguments(String[] args) {
            if(args.length>130 || args.length == 0){
                return false;
            }
            return true;
        }
}
