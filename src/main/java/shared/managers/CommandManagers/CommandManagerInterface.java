package shared.managers.CommandManagers;

import shared.Commands.AbstractCommand;
import shared.network.messaging.Request;
import shared.network.messaging.Response;


import java.sql.SQLException;
import java.util.Map;

public interface CommandManagerInterface {

    Map<String, ? extends AbstractCommand> getCommandsClient();
    Map<String, ? extends AbstractCommand> getCommandsServer();

    Request executeClient(String cmAndArgs);

    Response executeServer(Request request) throws SQLException;

}
