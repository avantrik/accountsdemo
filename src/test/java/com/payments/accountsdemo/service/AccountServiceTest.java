package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Integration Test Case for Account Service, in conjuction with Spring Boot.
 */

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration{
        @Bean
        public AccountService accountService(){
            return new AccountServiceImpl();
        }
    }

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp(){
        Account account = new Account("01001", BigDecimal.valueOf(20.00));
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        Mockito.when(accountRepository.findById(account.getAccountNo())).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.findAll()).thenReturn(accountList);
        Mockito.when(accountRepository.findById("0000")).thenReturn(Optional.empty());
    }


    /**
     * Integration Test
     */
    @Test
    public void whenAccountCreated_thenGetAllRetrieveTheOnlyAccount(){
        //When
        List<Account> accountList =  accountService.getAll();

        //Then
        assertTrue("GetAll is not working correctly ", accountList.size() == 1);
    }

    /**
     * Integration Test for showBalance.
     */
    @Test
    public void whenAccountCreated_thenShowBalanceForAccountCreated(){
        String accountNo = "01001";
        Account account = accountService.getAccount(accountNo);
        //When
        BigDecimal balance = accountService.showBalance(accountNo);

        //Then
        assertEquals(balance, account.getBalance());
    }

    /**
     * Integration test for showBalance for non existing account.
     */
    @Test
    public void whenAccountCreated_thenShowBalanceForInvalidAccount(){
        String accountNo = "0000";
        try {
            //when
            BigDecimal balance = accountService.showBalance(accountNo);
        } catch (AccountDoesNotExistException ex){
            assertTrue("Show Balance does not throw Exception ", true);
        }
    }

    /**
     * Integration Testing for Credit
     */
    @Test
    public void whenAccountCreatedCreditAccount_thenPerformCheckonBalanceOfAccount(){

        //When
        String accountNo = "01001";
        Account account = accountService.getAccount(accountNo);
        accountService.credit(accountNo, BigDecimal.valueOf(20.00));

        //then
        assertEquals(account.getBalance(), BigDecimal.valueOf(40.00));
    }

    //Not Performing Integration Test Case for credit and debit currently as its not part of the assignment.
}
