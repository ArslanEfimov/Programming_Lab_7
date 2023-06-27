package shared.network.messaging;

import shared.auth.Credentials;

import java.io.Serializable;
import java.util.Arrays;

public class RequestBody implements Serializable{
    private final String[] args;
    private Credentials credentials;

    public RequestBody(String[] args) {
        this.args = args;
    }

    public String[] getArgs(){
        return args;
    }

    public Credentials getCredentials(){
        return credentials;
    }
    public void setCredentials(Credentials credentials){
        this.credentials = credentials;
    }
    @Override
    public String toString() {
        return Arrays.toString(args);
    }
}
