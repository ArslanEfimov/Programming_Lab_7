package shared.managers.CommandManagers;


import shared.Commands.*;
import shared.auth.UserManager;
import shared.core.Exceptions.WrongValuesException;
import shared.io.UserIO;
import shared.managers.CollectionManager;
import shared.network.messaging.Request;
import shared.network.messaging.Response;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandsManager implements CommandManagerInterface{

    private HashMap<String, AbstractCommand> commandsMapClient;

    private HashMap<String, AbstractCommand> commandsMapServer;
    private UserIO userIO;
    private UserManager userManager;



    public CommandsManager(UserManager userManager){
        this.commandsMapClient = new HashMap<>();
        this.commandsMapServer = new HashMap<>();
        this.userIO = new UserIO();
        this.userManager = userManager;
    }
    public CommandsManager(){
        this.commandsMapClient = new HashMap<>();
        this.commandsMapServer = new HashMap<>();
        this.userIO = new UserIO();
    }

    @Override
    public Map<String, ? extends AbstractCommand> getCommandsClient() {
        return commandsMapClient;
    }
    @Override
    public Map<String, ? extends AbstractCommand> getCommandsServer() {
        return commandsMapServer;
    }
    @Override
    public Request executeClient(String cmAndArgs) {
        String[] args = cmAndArgs.trim().split("\\s+");
        String commandName = args[0];
        AbstractCommand command;
        int commandArgumentsStartPosition = 1;
        args = Arrays.copyOfRange(args, commandArgumentsStartPosition, args.length);
        command = commandsMapClient.get(commandName);
        try {
            return command.executeRequest(args);
        }catch (WrongValuesException ex){
            userIO.printerr(ex.getMessage());
            userIO.println(" ");
        }
        return null;
    }

    @Override
    public Response executeServer(Request request) throws SQLException {
        return commandsMapServer.get(request.getCommand().getName()).executeResponse(request.getRequestBody());
    }

    public void fillClientMapDefault(){
        HashMap<String, AbstractCommand> commandsMapClient = Arrays.asList(
                new InfoCommand(null),
                new ShowCommand(null),
                new ClearCommand(null),
                new RemoveFirstCommand(null),
                new PrintDescendingCommand(null),
                new RemoveByIdCommand(null),
                new FilterAnnualTurnoverCommand(null),
                new RemoveGreaterCommand(null),
                new CountGreaterThanOfficialAddressCommand(null),
                new AddCommand(null, userManager),
                new AddIfMaxCommand(null, userManager),
                new HelpCommand(null, userManager),
                new GetObjectForUpdateCommand(null, userManager),
                new ExitCommand(null),
                new ExecuteScriptCommand(null),
                new UpdateCommand(null, userManager),
                new RegisterCommand(null, userManager),
                new LoginCommand(null, userManager),
                new LogOutCommand(null, userManager)
        ).stream().collect(Collectors.toMap(AbstractCommand::getCommandName,
                Function.identity(),
                (existing, replacement) -> existing,
                HashMap::new));
        this.commandsMapClient = commandsMapClient;
    }
    public void fillClientMapAuth(){
        HashMap<String, AbstractCommand> commandsMapClient = Arrays.asList(
                new HelpCommand(null, userManager),
                new RegisterCommand(null, userManager),
                new LoginCommand(null, userManager),
                new LogOutCommand(null, userManager)
        ).stream().collect(Collectors.toMap(AbstractCommand::getCommandName,
                Function.identity(),
                (existing, replacement) -> existing,
                HashMap::new));
        this.commandsMapClient = commandsMapClient;
    }

    public void fillServerMapDefault(CollectionManager collectionManager){
        HashMap<String, AbstractCommand> commandsMapServer = Arrays.asList(
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new ClearCommand(collectionManager),
                new RemoveFirstCommand(collectionManager),
                new PrintDescendingCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new FilterAnnualTurnoverCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new CountGreaterThanOfficialAddressCommand(collectionManager),
                new AddCommand(collectionManager, userManager),
                new AddIfMaxCommand(collectionManager, userManager),
                new HelpCommand(collectionManager, userManager),
                new GetObjectForUpdateCommand(collectionManager, userManager),
                new ExitCommand(collectionManager),
                new ExecuteScriptCommand(collectionManager),
                new UpdateCommand(collectionManager, userManager),
                new RegisterCommand(collectionManager,userManager),
                new LoginCommand(collectionManager,userManager),
                new LogOutCommand(collectionManager, userManager)
        ).stream().collect(Collectors.toMap(AbstractCommand::getCommandName,
                Function.identity(),
                (existing, replacement) -> existing,
                HashMap::new));
        this.commandsMapServer = commandsMapServer;
    }

    public void fillServerMapAuth(){
        HashMap<String, AbstractCommand> commandsMapServer = Arrays.asList(
                new RegisterCommand(null,userManager),
                new LoginCommand(null,userManager),
                new HelpCommand(null, userManager),
                new LogOutCommand(null, userManager)
        ).stream().collect(Collectors.toMap(AbstractCommand::getCommandName,
                Function.identity(),
                (existing, replacement) -> existing,
                HashMap::new));
        this.commandsMapServer = commandsMapServer;
    }

    public boolean commandCheck(String command){
        if(commandsMapClient.containsKey(command)){
            return true;
        }
        else{
            userIO.printerr("Такой команды нет! Воспользуйтесь командой help, чтобы увидеть список возможных команд");
            return false;
        }
    }
}
