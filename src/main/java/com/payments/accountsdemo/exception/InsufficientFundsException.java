package com.payments.accountsdemo.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message){
        super(message);
    }

    public InsufficientFundsException(String message, Throwable throwable){
        super(message, throwable);
    }

}
