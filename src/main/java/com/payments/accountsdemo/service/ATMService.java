package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.exception.IncorrectAmountWithdrawnException;
import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.model.Withdrawal;

/**
 * ATMService depicts the various function of ATM.
 */
public interface ATMService {

    /**
     * Refills the ATM with notes of various denomination to pre-defined value.
     *
     */
    void replenish();

    /**
     * Display formatted String representation displaying in format defined in properties.
     * @return String representation of the balance.
     */
    String checkBalance(String accountNo) throws AccountDoesNotExistException;

    /**
     * Performs withdraw of specified funds from the accountNo specified.
     *
     *
     * @param accountNo
     * @param amount
     * @return
     * @throws AccountDoesNotExistException
     * @throws InsufficientFundsException
     */
    Withdrawal withdraw(String accountNo, int amount) throws AccountDoesNotExistException,
                                    InsufficientFundsException, IncorrectAmountWithdrawnException;
}
