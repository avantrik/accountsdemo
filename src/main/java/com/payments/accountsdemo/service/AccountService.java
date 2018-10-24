package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.persistence.entities.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service defining behaviours for Account
 */
public interface AccountService {

    /**
     * Provides the current balance for account provided
     * @param accountNo     Account No for which balance needs to be provided.
     * @return              Current balance of the account
     */
    BigDecimal showBalance(String accountNo) throws AccountDoesNotExistException;

    /**
     * Retrieve list of all Accounts available.
     * @return  List of Account Object.
     */
    List<Account> getAll();

    /**
     * Crediting Account with amount provided.
     * @param accountNo Account No to be credited with
     * @param amount    Amount to be credited into the account.
     * @throws AccountDoesNotExistException Raised when Account does not exists.
     */
    void credit(String accountNo, BigDecimal amount) throws AccountDoesNotExistException;

    /**
     * Debiting account with amount provided.
     * @param accountNo                     Account No which would be debited.
     * @param amount                        Amount to be debited
     * @throws AccountDoesNotExistException Raised when account does not exists
     * @throws InsufficientFundsException   Raised when amount to be debited is more then the balance in the account.
     */
    void debit(String accountNo, BigDecimal amount) throws AccountDoesNotExistException, InsufficientFundsException;

    /**
     * Retrieves Account for specified Account No, or else raise AccountDoesNotExistException
     * @param accountNo                     Account No specified
     * @throws AccountDoesNotExistException Raised when account does not exists.
     */
    Account getAccount(String accountNo) throws AccountDoesNotExistException;
}
