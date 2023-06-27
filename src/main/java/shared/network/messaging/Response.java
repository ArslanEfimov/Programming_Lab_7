package shared.network.messaging;

import shared.auth.UserState;

import java.io.Serializable;


public class Response implements Serializable {
    private String serverMessage;
    private boolean serverBol;
    private UserState userState;
    public Response (String serverMessage){
        this.serverMessage = serverMessage;
    }

    public Response (boolean serverBol){
        this.serverBol = serverBol;
    }
    public UserState getUserState(){
        return userState;
    }

    public void setUserState(UserState userState){
        this.userState = userState;
    }

    public Response(){}
    public String getServerMessage(){
        return serverMessage;
    }
    public void setServerMessage(String serverMessage){
        this.serverMessage = serverMessage;
    }

    @Override
    public String toString(){
        return serverMessage;
    }
}
