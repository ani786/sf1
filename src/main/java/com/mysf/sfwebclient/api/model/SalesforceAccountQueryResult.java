package com.mysf.sfwebclient.api.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SalesforceAccountQueryResult {
    private List<SalesforceAccount> records;

}

