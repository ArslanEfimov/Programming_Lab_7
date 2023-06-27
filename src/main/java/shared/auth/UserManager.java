package shared.auth;

import shared.network.messaging.Request;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import java.sql.SQLException;

public interface UserManager {

    Response register(RequestBody request) throws SQLException;
    Response login(RequestBody requestBody) throws SQLException;
    Response logOut(RequestBody requestBody);
    boolean checkIfRegistered(Request request);
    User findUser(Credentials credentials);
}
