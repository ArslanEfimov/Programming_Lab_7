package shared.Commands;


import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.core.models.Asker.AskerOrganizations;
import shared.core.models.Organization;
import shared.managers.CollectionManager;
import shared.network.factory.RequestFactory;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.RequestBodyOrganization;
import shared.network.messaging.Response;

public class AddIfMaxCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private AskerOrganizations askerOrganizations;
    private UserManager userManager;

    public AddIfMaxCommand(CollectionManager collectionManager, UserManager userManager) {
        super("add_if_max");
        this.collectionManager = collectionManager;
        this.askerOrganizations = new AskerOrganizations();
        this.userManager = userManager;

    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            Organization organization = askerOrganizations.setOrganization();
            return RequestFactory.createRequestOrganization(getCommandName(), null, organization);

        }
        else{
            throw new WrongValuesException("В команде add_if_max не должно быть аргументов");
        }

    }

    @Override
    public Response executeResponse(RequestBody requestBody) {
        Organization organization = ((RequestBodyOrganization) requestBody).getOrganization();
        organization.setId(collectionManager.generateId());
        if(collectionManager.greaterThanMax(organization)) {
            organization.setUserId(userManager.findUser(requestBody.getCredentials()).getId());
            organization.setUserName(userManager.findUser(requestBody.getCredentials()).getCredentials().getLogin());
            boolean ifAdd = collectionManager.addNewOrganization(organization);
            if(ifAdd==true) {
                return ResponseFactory.createResponse("Новый элемент успешно добавлен в коллекцию!");
            }
            else{
                return ResponseFactory.createResponse("Элемент не удалось добавить в коллекцию, попробуйте заново");
            }
        }
        else {
            return ResponseFactory.createResponse("Элемент не удалось добавить в коллекцию, " +
                    "так как его значенение (annulTurnover) меньше максимального элемента");
        }
    }

    @Override
    String getUsage() {
        return "add_if_max - добавляет новый элемент в коллекцию, если его значение annualTurnover больше наибольшего в коллекции";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
