package com.payments.accountsdemo.controller;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.model.WithdrawRequest;
import com.payments.accountsdemo.model.Withdrawal;
import com.payments.accountsdemo.service.ATMService;
import com.payments.accountsdemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * ATMController depicting MVC Spring Framework.
 * It would contain all the methods required for
 */
@Controller
@RequestMapping ("/api/atm")
public class ATMController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ATMService atmService;

    @GetMapping("/checkBalance/{accountNo}")
    public ResponseEntity<String> checkBalance(@PathVariable String accountNo) {
        return new ResponseEntity(atmService.checkBalance(accountNo), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Withdrawal> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Withdrawal withdrawal = atmService.withdraw(withdrawRequest.getAccountNo(), withdrawRequest.getAmount());
        return new ResponseEntity(withdrawal, HttpStatus.OK);
    }

    @PutMapping("/replenish")
    public ResponseEntity replenish(){
        atmService.replenish();
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
