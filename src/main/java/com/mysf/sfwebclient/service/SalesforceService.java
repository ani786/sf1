package com.mysf.sfwebclient.service;

import com.mysf.sfwebclient.api.model.SalesforceAccount;
import com.mysf.sfwebclient.api.model.SalesforceAccountQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class SalesforceService {

    private final OAuth2RestTemplate salesforceRestTemplate;

    public SalesforceService(OAuth2RestTemplate salesforceRestTemplate) {
        this.salesforceRestTemplate = salesforceRestTemplate;
    }

    public List<SalesforceAccount> getAccounts() {
        log.info( "before getAccounts service ......");
        String query = "SELECT Id, Name FROM Account LIMIT 10";
        String queryUrl = "/services/data/v57/query?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        SalesforceAccountQueryResult queryResult = salesforceRestTemplate.getForObject(queryUrl, SalesforceAccountQueryResult.class);
        return queryResult.getRecords();
    }
}
