package com.payments.accountsdemo.model;

import java.io.Serializable;

/**
 * Withdraw Request object, contains
 * 1. Account No
 * 2. Amount to withdraw
 */
public class WithdrawRequest implements Serializable {

    private String accountNo;
    private int amount;

    public WithdrawRequest(){

    }

    public WithdrawRequest(String accountNo, int amount){
        this.accountNo = accountNo;
        this.amount = amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WithdrawRequest{" +
                "accountNo='" + accountNo + '\'' +
                ", amount=" + amount +
                '}';
    }
}
