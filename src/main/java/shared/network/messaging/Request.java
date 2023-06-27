package shared.network.messaging;


import shared.auth.Credentials;
import shared.auth.UserState;
import shared.network.commandsdto.CommandDTO;

import java.io.Serializable;

public class Request implements Serializable {
    private CommandDTO command;
    private RequestBody requestBody;
    private RequestBodyAddress requestBodyAddress;
    private RequestBodyOrganization requestBodyOrganization;
    private UserState userState;
    private Credentials credentials;

    public Request(CommandDTO command, RequestBody requestBody){
        this.command = command;
        this.requestBody = requestBody;
    }
    public Request(CommandDTO command, RequestBodyAddress requestBodyAddress){
        this.command = command;
        this.requestBodyAddress = requestBodyAddress;
    }
    public Request(CommandDTO command, RequestBodyOrganization requestBodyOrganization){
        this.command = command;
        this.requestBodyOrganization = requestBodyOrganization;
    }




    public CommandDTO getCommand(){
        return command;
    }

    public RequestBody getRequestBody(){
        return requestBody;
    }

    public UserState getUserState(){
        return userState;
    }

    public void setUserState(UserState userState){
        this.userState = userState;
    }
    public Credentials getCredentials(){
        return credentials;
    }
    public void setCredentials(Credentials credentials){
        this.credentials = credentials;
    }
    @Override
    public String toString() {
        return String.valueOf(command) + ", аргументы команды: " + String.valueOf(requestBody);
    }
}
