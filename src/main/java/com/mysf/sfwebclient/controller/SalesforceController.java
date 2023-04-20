package com.mysf.sfwebclient.controller;

import com.mysf.sfwebclient.api.model.SalesforceAccountQueryResult;
import com.mysf.sfwebclient.service.SalesforceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/salesforce")
@Slf4j
public class SalesforceController {

    private final SalesforceService salesforceService;

    public SalesforceController(SalesforceService salesforceService) {
        this.salesforceService = salesforceService;
    }

    @GetMapping("/accounts")
    public Flux<SalesforceAccountQueryResult> getAccounts() {
        log.debug("before getAccounts controller ......");

        return salesforceService.getAccounts();
    }
}

