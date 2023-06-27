package shared.auth;

public class User {

    private int id;
    private Credentials credentials;

    public User(int id, Credentials credentials){
        this.id = id;
        this.credentials = credentials;
    }

    public User (Credentials credentials){
        this.credentials = credentials;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public Credentials getCredentials(){
        return credentials;
    }
    public void setCredentials(Credentials credentials){
        this.credentials = credentials;
    }

}
