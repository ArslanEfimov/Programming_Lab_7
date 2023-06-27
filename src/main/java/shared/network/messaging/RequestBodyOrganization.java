package shared.network.messaging;

import shared.core.models.Organization;

public class RequestBodyOrganization extends RequestBody{

    private final Organization organization;
    public RequestBodyOrganization(String[] args, Organization organization) {
        super(args);
        this.organization = organization;
    }

    public Organization getOrganization(){
        return organization;
    }
}
