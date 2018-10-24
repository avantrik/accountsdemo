package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Account Service Implementation, providing list of all the methods for Accounts.
 * It uses AccountRepository for performing any JPA activity.
 */
@Service
public class AccountServiceImpl implements AccountService{

    /*
    *   LOGGER Instance.
     */
    private static Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    /*
    *   Account Repository.
     */
    @Autowired
    AccountRepository accountRepository;

    /**
     * @Link AccountService#showBalance()
     */
    @Override
    public BigDecimal showBalance(String accountNo) throws AccountDoesNotExistException {
        LOG.info("Inside ShowBalance for Account: {}", accountNo);
        Account account = getAccount(accountNo);
        return account.getBalance();
    }

    /**
     * Retrieve All the accounts.
     * @return List of Accounts.
     */
    @Override
    public List<Account> getAll() {
        LOG.info("Inside getAll for Account Service ");
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(a -> accountList.add(a));
        return accountList;
    }

    /**
     * Performing Credit on a account, for specified amount.
     * @param accountNo Account No passed across and searched.
     * @param amount    Amount to be credited.
     * @throws AccountDoesNotExistException     Account specified by account no does not exist in the system.
     */
    @Override
    public void credit(String accountNo, BigDecimal amount) throws AccountDoesNotExistException {
        LOG.info("Inside credit for Account Service for account {} , for amount {}", accountNo, amount.longValue());
        Account account = getAccount(accountNo);
        account.deposit(amount);
        accountRepository.save(account);
        LOG.info("Completed credit for account: {}, for amount {} ", accountNo, amount.longValue());
    }

    /**
     * Debit Service for a sepecific account by amount specified.
     * @param accountNo                     Account No specified.
     * @param amount                        Amount Specified.
     * @throws AccountDoesNotExistException If account does not exist
     * @throws InsufficientFundsException   Sufficient fund does not exist for amount to be debited.
     */
    @Override
    public void debit(String accountNo, BigDecimal amount) throws AccountDoesNotExistException, InsufficientFundsException {
        LOG.info("Inside debit for Account Service for Account {}, for amount {} ", accountNo, amount);
        Account account = getAccount(accountNo);
        account.withdraw(amount);
        accountRepository.save(account);
        LOG.info("Completed debit for account : {}, for amount {}", accountNo, amount);
    }

    /**
     * Retrieve Account Details.
     * @param accountNo                     Account No specified
     * @return Account Object providing all information related to account.
     * @throws AccountDoesNotExistException Raised when Account is not found.
     */
    @Override
    public Account getAccount(String accountNo) throws AccountDoesNotExistException {
        return  accountRepository.findById(accountNo).orElseThrow(() -> new AccountDoesNotExistException("Account does not exists"));
    }
}
