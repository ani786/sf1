package com.mysf.sfwebclient.controller;

import com.mysf.sfwebclient.api.model.SalesforceAccount;
import com.mysf.sfwebclient.service.SalesforceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/salesforce")
@Slf4j
public class SalesforceController {

    private final SalesforceService salesforceService;

    public SalesforceController(SalesforceService salesforceService) {
        this.salesforceService = salesforceService;
    }

    @GetMapping("/accounts")
    public List<SalesforceAccount> getAccounts() {
        log.info( " before getAccounts controller ......");

        return salesforceService.getAccounts();
    }
}

