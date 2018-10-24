package com.payments.accountsdemo.model;

import com.payments.accountsdemo.persistence.entities.Denomination;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * This is model class, depiciting the withdrawal transfer object containing
 * 1. Date of Transaction
 * 2. Account No
 * 3. Balance before the withdrawal
 * 4. Balance after withdrawal
 * 5. List of denomination object.
 *
 * This class needs to be final, as it should not be changed once created.
 *
 */
public class Withdrawal {

    private LocalDateTime time;
    private String     accountNo;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    public long amountWithdrawn;
    //Need to be array of not more then 4, and not to be modified.
    private Denomination[] denominations;

    public Withdrawal(){

    }

    /**
     * Constructor for Withdrawal Object.
     * Should we be using builder pattern, inorder to ensure
     * @param accountNo
     * @param amountWithdrawn
     */
    public Withdrawal(String accountNo, long amountWithdrawn){
        this.time = LocalDateTime.now();
        this.accountNo = accountNo;
        this.amountWithdrawn = amountWithdrawn;
        this.denominations = new Denomination[4];
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public long getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public Denomination[] getDenominations() {
        return denominations;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public void setAmountWithdrawn(long amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }

    public void setDenominations(Denomination[] denominations) {
        this.denominations = denominations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Withdrawal that = (Withdrawal) o;
        return amountWithdrawn == that.amountWithdrawn &&
                Objects.equals(time, that.time) &&
                Objects.equals(accountNo, that.accountNo) &&
                Objects.equals(balanceBefore, that.balanceBefore) &&
                Objects.equals(balanceAfter, that.balanceAfter) &&
                Arrays.equals(denominations, that.denominations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(time, accountNo, balanceBefore, balanceAfter, amountWithdrawn);
        result = 31 * result + Arrays.hashCode(denominations);
        return result;
    }

    @Override
    public String toString() {
        return "Withdrawal{" +
                "time=" + time +
                ", accountNo=" + accountNo +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                ", amountWithdrawn=" + amountWithdrawn +
                ", denominations=" + Arrays.toString(denominations) +
                '}';
    }
}
