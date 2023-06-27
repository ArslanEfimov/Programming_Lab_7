package shared.network.messaging;

import shared.core.models.Address;

public class RequestBodyAddress extends RequestBody {

    private final Address address;

    public RequestBodyAddress(String[] args, Address address){
        super(args);
        this.address = address;

    }

    public Address getAddress(){
        return address;
    }

}
