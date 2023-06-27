package shared.network.factory;

import shared.core.models.Address;
import shared.core.models.Organization;
import shared.network.messaging.RequestBody;
import shared.network.messaging.RequestBodyAddress;
import shared.network.messaging.RequestBodyOrganization;

public class RequestBodyFactory {

    public RequestBodyFactory(){
    }

    public static RequestBody createRequestBody(String[] args){
        return new RequestBody(args);
    }

    public static RequestBody createRequestBodyAddress(String[] args, Address address){
        return new RequestBodyAddress(args, address);
    }

    public static RequestBody createRequestBodyOrganization(String[] args, Organization organization){
        return new RequestBodyOrganization(args, organization);
    }
}
