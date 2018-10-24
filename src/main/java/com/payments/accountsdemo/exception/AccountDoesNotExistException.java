package com.payments.accountsdemo.exception;

/**
 * Depicts AccountDoesNotExistException
 *
 */
public class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException(String message){
        super(message);
    }

    public AccountDoesNotExistException(String message, Throwable throwable){
        super(message, throwable);
    }

}
