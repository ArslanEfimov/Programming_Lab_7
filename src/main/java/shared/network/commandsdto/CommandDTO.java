package shared.network.commandsdto;

import java.io.Serializable;

public class CommandDTO implements Serializable {
    private String name;

    public CommandDTO(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    @Override
    public String toString(){
        return "Имя команды: " + name;
    }
}
