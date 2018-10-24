package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.exception.IncorrectAmountWithdrawnException;
import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.model.Notes;
import com.payments.accountsdemo.model.Withdrawal;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.entities.Denomination;
import com.payments.accountsdemo.persistence.repository.AccountRepository;
import com.payments.accountsdemo.persistence.repository.DenominationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Integration Test for ATMService
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class ATMServiceTest {

    private static Logger LOG = LoggerFactory.getLogger("ATMServiceTest");

    @TestConfiguration
    static class ATMServiceImplTestContextConfiguration{
        @Bean
        public ATMService aTMService(){
            return new ATMServiceImpl();
        }

        @Bean
        public AccountService accountService(){
            return new AccountServiceImpl();
        }
    }

    @Autowired
    private ATMService atmService;

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private DenominationRepository denominationRepository;


    @Before
    public void setUp(){
        Account account = new Account("01001", BigDecimal.valueOf(2000.00));
        List<Account> accountList = new ArrayList<>();

        accountList.add(account);
        Mockito.when(accountRepository.findById(account.getAccountNo())).thenReturn(Optional.of(account));
    }

    @Test
    public void whenCheckBalance_thenProvideCorrectString(){
        LOG.info("Inside whenCheckBalance_thenProvideCorrectString");
        //When
        String accountNo = "01001";
        String strBalance = atmService.checkBalance(accountNo);

        //Then
        assertEquals(strBalance, "£2000.00");
    }

    /**
     * Testing checkBalance for Invalid Account no.
     */
    @Test
    public void whenCheckBalanceForInvalidAccount_thenRaiseException(){
        //When
        String accountNo = "00000";
        try {
            String strBalance = atmService.checkBalance(accountNo);
        } catch (AccountDoesNotExistException ex){
            assertTrue("Check Balance is not checking account exists ", true);
        }

    }

    /**
     * Testing withdrawal with valid account
     */
    @Test
    public void whenWithdrawValidAmount_ThenProvideWithdrawal(){
        LOG.info("Iniside  whenWithdrawValidAmount_ThenProvideWithdrawal ");
        String accountNo = "01001";
        int withdrawAmount = 215;

        //Setting up £400 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 2));
        denominationList.add(new Denomination(Notes.TWENTY, 5));
        denominationList.add(new Denomination(Notes.TEN, 5));
        denominationList.add(new Denomination(Notes.FIVE, 10));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);



        Withdrawal withdrawal  = atmService.withdraw(accountNo, withdrawAmount);
        LOG.info("Withdrawal data {}", withdrawal );

        assertTrue(withdrawal.getDenominations().length==4);

    }

    /**
     * Unit Test withdraw on ATMService with invalid account No.
     */
    @Test
    public void whenWithdrawInvalidAccountNo_thenRaiseException(){
        LOG.info("Iniside  whenWithdrawInvalidAccountNo_thenRaiseException ");
        String accountNo = "00000";
        int withdrawAmount = 215;
        try {
            Withdrawal withdrawal = atmService.withdraw(accountNo, withdrawAmount);
        }catch (AccountDoesNotExistException ex){
            assertTrue("Account validation has failed ", true);
        }

    }

    /**
     * Unit Testing various withdrawal request of different types.
     * This would lead to lower denomination and checking when ATM Service goes with InsufficientFundsException.
     *
     * Cases to be tested.
     *
     * 1. Test with less amount
     * 2. Test with greater Amount
     * 3. Test withdraw from
     */
    @Test
    public void whenWithdrawAmountWithInsufficientFundsInATM_thenRaiseExceptionAndCheckIt(){

        //Setting up £200 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 1));
        denominationList.add(new Denomination(Notes.TWENTY, 2));
        denominationList.add(new Denomination(Notes.TEN, 3));
        denominationList.add(new Denomination(Notes.FIVE, 10));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);

        String accountNo = "01001";
        int withdrawAmount = 215;

        try {
            Withdrawal withdrawal = atmService.withdraw(accountNo, withdrawAmount);
            LOG.info("Withdrawal data {}", withdrawal );
        }catch (InsufficientFundsException ex){
            assertTrue("Computation of total from denomination is not correct", true);
        }
    }

    @Test
    public void whenWithdrawWith50NoteNotPreset_thenCheckWithdrawal(){
        LOG.info("Iniside  whenWithdrawValidAmount_ThenProvideWithdrawal ");
        String accountNo = "01001";
        int withdrawAmount = 215;

        //Setting up £400 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 0));
        denominationList.add(new Denomination(Notes.TWENTY, 20));
        denominationList.add(new Denomination(Notes.TEN, 10));
        denominationList.add(new Denomination(Notes.FIVE, 10));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);

        Withdrawal withdrawal  = atmService.withdraw(accountNo, withdrawAmount);
        LOG.info("Withdrawal data {}", withdrawal );

        assertTrue(withdrawal.getDenominations().length==3);

    }

    @Test
    public void whenWithdrawWith20NoteNotPresent_thenCheckWithdrawal(){
        LOG.info("Iniside  whenWithdrawValidAmount_ThenProvideWithdrawal ");
        String accountNo = "01001";
        int withdrawAmount = 215;

        //Setting up £400 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 10));
        denominationList.add(new Denomination(Notes.TWENTY, 0));
        denominationList.add(new Denomination(Notes.TEN, 20));
        denominationList.add(new Denomination(Notes.FIVE, 10));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);

        Withdrawal withdrawal  = atmService.withdraw(accountNo, withdrawAmount);
        LOG.info("Withdrawal data {}", withdrawal );

        assertTrue(withdrawal.getDenominations().length==3);

    }

    @Test
    public void whenWithdrawWith10NoteNotPresent_thenCheckWithdrawal(){
        LOG.info("Iniside  whenWithdrawValidAmount_ThenProvideWithdrawal ");
        String accountNo = "01001";
        int withdrawAmount = 215;

        //Setting up £400 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 10));
        denominationList.add(new Denomination(Notes.TWENTY, 10));
        denominationList.add(new Denomination(Notes.TEN, 0));
        denominationList.add(new Denomination(Notes.FIVE, 10));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);

        Withdrawal withdrawal  = atmService.withdraw(accountNo, withdrawAmount);
        LOG.info("Withdrawal data {}", withdrawal );

        assertTrue(withdrawal.getDenominations().length==3);

    }

    @Test
    public void whenWithdrawWith5NoteNotPresent_thenCheckWithdrawal(){
        LOG.info("Iniside  whenWithdrawValidAmount_ThenProvideWithdrawal ");
        String accountNo = "01001";
        int withdrawAmount = 215;

        //Setting up £400 into the ATM such that various cases can be tested.
        List<Denomination> denominationList = new ArrayList<>();
        denominationList.add(new Denomination(Notes.FIFTY, 10));
        denominationList.add(new Denomination(Notes.TWENTY, 10));
        denominationList.add(new Denomination(Notes.TEN, 5));
        denominationList.add(new Denomination(Notes.FIVE, 0));

        Mockito.when(denominationRepository.findAll()).thenReturn(denominationList);

        try {
            Withdrawal withdrawal = atmService.withdraw(accountNo, withdrawAmount);
            LOG.info("Withdrawal data {}", withdrawal);
        }catch (IncorrectAmountWithdrawnException ex){
            assertTrue("Incorrect computation of denomination", true);
        }
    }

}
