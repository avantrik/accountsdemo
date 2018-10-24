package com.payments.accountsdemo.exception;

public class ApplicationNotInitialisedException extends RuntimeException {

    public ApplicationNotInitialisedException(String message){
        super(message);
    }

    public ApplicationNotInitialisedException(String message, Throwable throwable){
        super(message, throwable);
    }
}
