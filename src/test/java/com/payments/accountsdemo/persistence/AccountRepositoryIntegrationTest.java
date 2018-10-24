package com.payments.accountsdemo.persistence;

import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {


    @Autowired
    private AccountRepository accountRepository;

    /**
     * Testing retrieval of Account
     */
    @Test
    public void whenFindByAccountNo_thenReturnAccount(){
        //given
        Account account = new Account("01001", BigDecimal.valueOf(2000.32));
        accountRepository.save(account);

        //When
        Optional<Account> optionalAccount = accountRepository.findById(account.getAccountNo());

        Account found = optionalAccount.get();

        //then
        assertTrue("Accounts do not match ", account.getAccountNo().equals(found.getAccountNo())
                                                && account.getBalance().equals(found.getBalance()));
    }

    /**
     * Finding invalid Account No.
     */
    @Test
    public void whenFindByAccountNoInvalid_thenReturnException(){
        //Given
        Account account = new Account("01011", BigDecimal.valueOf(200.32));
        accountRepository.save(account);
        String invalidAccountNo = "0000";

        //When
        Optional<Account> optionalAccount = accountRepository.findById(invalidAccountNo);

        //Then
        assertFalse("Find is not working ", optionalAccount.isPresent());

    }

    /**
     * Integration Test for crediting the account
     */
    @Test
    public void whenCreditAccount_ThenReturnAccountWithNewBalance(){

        //Given Account
        Account account = new Account("01101", BigDecimal.valueOf(100.00));
        accountRepository.save(account);

        //When Find the Account
        account.deposit(BigDecimal.valueOf(50.00));
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getAccountNo());

        //Then
        assertTrue("Credit Functionality does not work ", account.getBalance().equals(optionalAccount.get().getBalance()));

    }

    @Test
    public void whenDebitAccount_ThenReturnAccountWithNewBalance(){
        //Given Account
        Account account = new Account("01201", BigDecimal.valueOf(300.00));
        accountRepository.save(account);

        //When Find the Account
        account.withdraw(BigDecimal.valueOf(50.00));
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getAccountNo());

        //Then
        assertTrue("Withdraw Functionality does not work ", account.getBalance().equals(optionalAccount.get().getBalance()));

    }

    @Test
    public void whenDebitAccountWithMoreThenBalance_ThenRaiseException(){

        //Given Account
        Account account = new Account("01301", BigDecimal.valueOf(100.00));
        accountRepository.save(account);

        try {
            //When Find the Account
            account.withdraw(BigDecimal.valueOf(150.00));
            accountRepository.save(account);

            assertFalse("Exception was to be raised, there is a problem with withdraw functionality", true);
        }catch (InsufficientFundsException ifEx){
            assertTrue("Insufficient fund exception ", true);
        }
    }


}
