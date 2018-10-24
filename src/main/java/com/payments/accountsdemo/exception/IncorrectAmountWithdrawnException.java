package com.payments.accountsdemo.exception;

/**
 * Application Exception raised when amount withdrawn is less then minimum or more then maximum.
 */
public class IncorrectAmountWithdrawnException extends RuntimeException {

    public IncorrectAmountWithdrawnException(String message){
        super(message);
    }

    public IncorrectAmountWithdrawnException(String message, Throwable throwable){
        super(message, throwable);
    }
}
