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

import java.sql.SQLException;

public class GetObjectForUpdateCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private AskerOrganizations askerOrganizations;
    private UserManager userManager;

    public GetObjectForUpdateCommand(CollectionManager collectionManager, UserManager userManager) {
        super("getId");
        this.collectionManager = collectionManager;
        this.askerOrganizations = new AskerOrganizations();
        this.userManager = userManager;

    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)) {
                Organization organization = askerOrganizations.setOrganization();
                return RequestFactory.createRequestOrganization(getCommandName(), args, organization);
        }

        else{
            throw new WrongValuesException("В команде update_id должны быть аргументы типа (int)");
        }

    }

    @Override
    public Response executeResponse(RequestBody requestBody) throws SQLException {
        Long id = Long.parseLong(requestBody.getArgs()[0]);
        if(collectionManager.getCollectionVector().size()!=0) {
            Organization organization = ((RequestBodyOrganization) requestBody).getOrganization();
            organization.setId(id);
            organization.setUserId(userManager.findUser(requestBody.getCredentials()).getId());
            organization.setUserName(userManager.findUser(requestBody.getCredentials()).getCredentials().getLogin());
                if (collectionManager.updateElement(organization) == true) {
                    synchronized (this) {
                        collectionManager.getCollectionVector().removeIf(elem -> elem.getId() == id);
                        collectionManager.addNewElement(organization);
                    }
                    return ResponseFactory.createResponse("Элемент успешно обновлен!");
                } else {
                    return ResponseFactory.createResponse("У вас пока что нет собственных объектов");
                }

            }
        else{
                return ResponseFactory.createResponse("Коллекция пуста!");
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
