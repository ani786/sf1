package com.mysf.sfwebclient.api.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SalesforceAccount {
    private String id;
    private String name;
    private String industry;
    private String type;
    private String billingCity;
    private String billingState;
    private String billingCountry;

}

