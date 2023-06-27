package shared.Commands;

import shared.auth.Credentials;
import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.managers.CollectionManager;
import shared.managers.CommandManagers.CommandsManager;
import shared.network.commandsdto.CommandDTO;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.util.ArrayList;

public class HelpCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private CommandsManager commandsManager;
    private UserManager userManager;
    public HelpCommand(CollectionManager collectionManager, UserManager userManager) {
        super("help");
        this.collectionManager = collectionManager;
        commandsManager = new CommandsManager();
        this.userManager = userManager;
    }

    @Override
    public Request executeRequest(String[] args) throws WrongValuesException {
        if(checkArguments(args)){
            return super.executeRequest(null);
        }
        else{
            throw new WrongValuesException("В команде help не должно быть аргументов!");
        }
    }

    @Override
    public Response executeResponse(RequestBody requestBody) {
        Request request = new Request(new CommandDTO(getCommandName()),requestBody);
        request.setCredentials(requestBody.getCredentials());
        if(userManager.checkIfRegistered(request)){
            commandsManager.fillServerMapDefault(collectionManager);
        }
        else{
            commandsManager.fillServerMapAuth();
        }
        ArrayList<String> commandHelpList = new ArrayList<>();
        commandsManager.getCommandsServer().values().forEach(command -> {
            String usage = command.getUsage();
            commandHelpList.add(usage);
        });
        return ResponseFactory.createResponse(String.join("\n",commandHelpList));
    }

    @Override
    String getUsage() {
        return "help - выводит список всех команд с их описанием";
    }

    @Override
    public boolean checkArguments(String[] args) {
        return args.length==0;
    }
}
