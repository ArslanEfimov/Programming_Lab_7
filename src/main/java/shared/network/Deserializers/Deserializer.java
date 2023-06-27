package shared.network.Deserializers;

import shared.core.Exceptions.DeserializeException;
import shared.network.messaging.Request;
import shared.network.messaging.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserializer {

    public synchronized static Request deserializeRequest(byte[] serializedObject){
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Request) objectInputStream.readObject();
        }catch (IOException |  ClassNotFoundException ex){
            throw new DeserializeException("десериализовать запрос не удается!");
        }
    }
    public synchronized static Response deserializeResponse(byte[] serializedObject){
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Response) objectInputStream.readObject();
        }catch (IOException |  ClassNotFoundException ex){
            throw new DeserializeException("десериализовать ответ не удается!");
        }
    }
}
