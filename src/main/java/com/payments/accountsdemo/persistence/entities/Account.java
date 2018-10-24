package com.payments.accountsdemo.persistence.entities;


import com.payments.accountsdemo.exception.InsufficientFundsException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity Account object used for persisting Account information.
 */
@Entity
@Table(name="ACCOUNT" )
public class Account {

    @Id
    @Column(name = "ACCOUNTNO")
    private String accountNo;

    @Column (name = "BALANCE", nullable = false)
    private BigDecimal balance;

    /**
     * Default Constructor
     */
    public Account(){

    }

    /**
     * Constructor for Account with Account No.
     * @param accountNo
     */
    public Account(String accountNo){
        this.accountNo = accountNo;
        this.balance = new BigDecimal(0);
    }

    /**
     * Account constructor with Account No and Deposit.
     * @param accountNo
     * @param deposit
     */
    public Account(String accountNo, BigDecimal deposit) {
        this.accountNo = accountNo;
        this.balance = deposit;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public void deposit(BigDecimal amount){
        balance = balance.add(amount);
    }

    /**
     * Withdraw amount from Account balance
     * @param amount
     * @throws InsufficientFundsException
     */
    public void withdraw(BigDecimal amount) throws InsufficientFundsException {
        if(balance.intValue() <= 0 ) {
            throw new InsufficientFundsException("Funds are less then viable options");
        }
        if(balance.longValue() - amount.longValue() <= 0){
            throw new InsufficientFundsException("In Sufficient funds in the account. Please withdraw a lesser amount ");
        }
        this.balance = balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNo, account.accountNo) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNo, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo=" + accountNo +
                ", balance=" + balance +
                '}';
    }
}
