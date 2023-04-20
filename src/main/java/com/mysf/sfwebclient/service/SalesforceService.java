package com.mysf.sfwebclient.service;

import com.mysf.sfwebclient.api.model.SalesforceAccountQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class SalesforceService {

    private final WebClient salesforceWebClient;

    public SalesforceService(WebClient salesforceWebClient) {
        this.salesforceWebClient = salesforceWebClient;
    }
    public Flux<SalesforceAccountQueryResult> getAccounts() {
        log.info( "before getAccounts service ......");
        String query = "SELECT Id, Name FROM Account LIMIT 10";
        String queryUrl = "/services/data/v57.0/query?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return salesforceWebClient.get()
                .uri(queryUrl)
                .retrieve()
                .bodyToFlux(SalesforceAccountQueryResult.class);
    }
}
