package com.payments.accountsdemo.controller;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Accounts Controller, depicting the Spring MVC model.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method= RequestMethod.GET, value="/showBalance/{accountNo}", produces = "application/json")
    public ResponseEntity<String> showBalance(@PathVariable String accountNo) throws AccountDoesNotExistException {
        BigDecimal balance = accountService.showBalance(accountNo);
        return new ResponseEntity(String.valueOf(balance.doubleValue()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = "application/json")
    public ResponseEntity<List<Account>> getAll() {
        return new ResponseEntity(accountService.getAll(), HttpStatus.OK);
    }


}
