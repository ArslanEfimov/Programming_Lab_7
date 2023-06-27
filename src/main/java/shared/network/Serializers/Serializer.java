package shared.network.Serializers;


import shared.core.Exceptions.SerializeException;
import shared.network.messaging.Request;
import shared.network.messaging.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer {

    public static byte[] serializeRequest(Request request){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }catch (IOException ex){
            throw new SerializeException("не удается сериализовать запрос!");
        }
    }

    public static byte[] serializeResponse(Response response){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }catch (IOException ex){
            throw new SerializeException("не удается сериализовать ответ!");
        }
    }
}
