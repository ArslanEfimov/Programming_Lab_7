package shared.network.factory;

import shared.core.models.Address;
import shared.core.models.Organization;
import shared.network.commandsdto.CommandDTO;
import shared.network.messaging.Request;


public class RequestFactory {
    public RequestFactory(){}

    public static Request createRequest(String command, String[] args){
        return new Request(new CommandDTO(command), RequestBodyFactory.createRequestBody(args));
    }

    public static Request createRequestAddress(String command, String[] args, Address address){
        return new Request(new CommandDTO(command), RequestBodyFactory.createRequestBodyAddress(args, address));
    }
    public static Request createRequestOrganization(String command, String[] args, Organization organization){
        return new Request(new CommandDTO(command), RequestBodyFactory.createRequestBodyOrganization(args, organization));
    }
}
