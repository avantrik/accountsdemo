package com.payments.accountsdemo.service;

import com.payments.accountsdemo.exception.AccountDoesNotExistException;
import com.payments.accountsdemo.exception.ApplicationNotInitialisedException;
import com.payments.accountsdemo.exception.IncorrectAmountWithdrawnException;
import com.payments.accountsdemo.exception.InsufficientFundsException;
import com.payments.accountsdemo.model.Notes;
import com.payments.accountsdemo.model.Withdrawal;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.entities.Denomination;
import com.payments.accountsdemo.persistence.repository.DenominationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * ATM Service Implementation Class, providing init for initialsing denomination repository.
 *
 * @author Chirag Tailor
 */
@Service
@Primary
public class ATMServiceImpl implements ATMService {

    private static Logger LOG = LoggerFactory.getLogger(ATMServiceImpl.class);

    @Autowired
    AccountService accountService;

    @Autowired
    DenominationRepository denominationRepository;

    @Value("${payments.denomination.size}")
    private int defaultDenominationSize;

    @Value("${payments.withdraw.limit.minimum}")
    private int minimumWithdrawLimit;

    @Value("${payments.withdraw.limit.maximum}")
    private int maximumWithdrawLimit;

    Map<Notes, Denomination> mapOfDenomination = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        LOG.info("Initialsing Map Of denomination after construction of Service Bean.");
        denominationRepository.findAll().forEach(d -> mapOfDenomination.put(d.getNotes(), d));
    }

    /**
     * Replenishing the ATM with all the denomination.
     */
    @Override
    public void replenish() {
        LOG.info("Inside replenish for ATM");
        denominationRepository.findAll().forEach(d -> {d.reset(defaultDenominationSize); denominationRepository.save(d);});
    }

    /**
     * Provide Balance for the account in a default way
     */
    @Override
    public String checkBalance(String accountNo) throws AccountDoesNotExistException {
        LOG.info("Inside checkBalance of ATMService for Account No: {}", accountNo);
        Account account = accountService.getAccount(accountNo);
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        return currencyInstance.format(account.getBalance());
    }

    /**
     * Perform withdraw of Amount on an account, returning Withdrawal object.
     * @param accountNo     Account No from which to withdraw
     * @param amount        Amount to be withdrawn.
     * @return
     * @throws AccountDoesNotExistException
     * @throws InsufficientFundsException
     * @throws IncorrectAmountWithdrawnException
     */
    @Override
    public Withdrawal withdraw(String accountNo, int amount) throws AccountDoesNotExistException,
                                                        InsufficientFundsException, IncorrectAmountWithdrawnException {
        LOG.info("Inside withdraw method in ATMServiceImpl.. Account No : {} and Amount: {}", accountNo, amount);
        if(mapOfDenomination.isEmpty()) init();
        Withdrawal withdrawal = new Withdrawal(accountNo, amount);
        LOG.debug("Performing Validation for Account & Amount");
        //Validating Account & Amount
        validateAccountAndAmount(accountNo, amount);

        LOG.debug("Retriving Account Details ");
        Account account = accountService.getAccount(accountNo);
        LOG.debug("Constructing Withdrawal Object");
        withdrawal.setBalanceBefore(account.getBalance());

        LOG.debug("Debiting Amount from the account using Account Service");
        accountService.debit(accountNo, BigDecimal.valueOf(amount));

        //Computing Denomination, ensuring 5 denomination atleast.
        Denomination[] denominations = computeDenominationForAmount(amount);
        withdrawal.setDenominations(denominations);
        LOG.debug("Updating Denomination in the map of Denomination.");
        for (Denomination denomination : denominations) {
            Denomination denominationFromMap = mapOfDenomination.get(denomination.getNotes());
            denominationFromMap.reduceNoteCount(denomination.getCountOfNotes());
        }

        LOG.debug("Updating Denomination.. ");
        denominationRepository.saveAll(mapOfDenomination.values());
        Account newAccount = accountService.getAccount(accountNo);
        withdrawal.setBalanceAfter(newAccount.getBalance());
        return withdrawal;
    }

    /**
     * Making calculation for the denomination
     * @param amount    Amount to be withdrawn
     * @return          Array of Denomination Object.
     */
    private Denomination[] computeDenominationForAmount(int amount){
        LOG.info("Inside compute denomination for Amount {}", amount);
        //Perform check whether we have 5 pound denomination with us, if so, deliver.
//        if(mapOfDenomination.isEmpty()) throw new ApplicationNotInitialisedException("Application is not initialised correctly.");
        if(mapOfDenomination.isEmpty()) {init();}
        if(mapOfDenomination.get(Notes.FIVE).getCountOfNotes() > 1 ) {
            amount -= 5;
        }
        int remainder = amount;
        LOG.debug("Subtracting 5 from amount to ensure atleast one £5 denomination to be dispensed.");
        int noOfNotes50 = amount / 50;
        if(mapOfDenomination.get(Notes.FIFTY).getCountOfNotes() >= noOfNotes50 ) {
            remainder = remainder % 50;
        }else{
            Denomination fiftyDenomination = mapOfDenomination.get(Notes.FIFTY);
            noOfNotes50 = fiftyDenomination.getCountOfNotes();
            remainder -= fiftyDenomination.getAmount();
        }

        int noOfNotes20 = remainder / 20;
        if(mapOfDenomination.get(Notes.TWENTY).getCountOfNotes() >= noOfNotes20 ) {
            remainder = remainder % 20;
        }else{
            Denomination twentyDenomination = mapOfDenomination.get(Notes.TWENTY);
            noOfNotes20 = twentyDenomination.getCountOfNotes();
            remainder -= twentyDenomination.getAmount();
        }

        int noOfNotes10 = remainder / 10;
        if(mapOfDenomination.get(Notes.TEN).getCountOfNotes() >= noOfNotes10 ) {
            remainder = remainder % 10;
        }else {
            Denomination tenDenomination = mapOfDenomination.get(Notes.TEN);
            noOfNotes10 = tenDenomination.getCountOfNotes();
            remainder -= tenDenomination.getAmount();
        }

        int noOfNotes5 = remainder / 5;
        if(mapOfDenomination.get(Notes.FIVE).getCountOfNotes() >= noOfNotes5 ) {
            remainder = remainder % 5;
        }else {
            Denomination fiveDenomination = mapOfDenomination.get(Notes.FIVE);
            noOfNotes5 = fiveDenomination.getCountOfNotes();
            remainder -= fiveDenomination.getAmount();
        }

        //Incrementing noOfNotes5 by 1 for initial deduction of £5
        noOfNotes5++;

        List<Denomination> denominationList = new ArrayList<>(4);
        if(noOfNotes50 > 0) {
            Denomination denominationFor50 = new Denomination(Notes.FIFTY, noOfNotes50);
            LOG.debug("Fify Denomination added {}", denominationFor50);
            denominationList.add(denominationFor50);
        }
        if(noOfNotes20 > 0){
            Denomination denominationFor20 = new Denomination(Notes.TWENTY, noOfNotes20);
            LOG.debug("Twenty Denomination added {}", denominationFor20);
            denominationList.add(denominationFor20);
        }

        if(noOfNotes10 > 0) {
            Denomination denominationFor10 = new Denomination(Notes.TEN, noOfNotes10);
            LOG.debug("Ten Denomination added {}", denominationFor10);
            denominationList.add(denominationFor10);
        }

        if(noOfNotes5 > 0) {
            Denomination denominationFor5 = new Denomination(Notes.FIVE, noOfNotes5);
            LOG.debug("Twenty Denomination added {}", denominationFor5);
            denominationList.add(denominationFor5);
        }

        return denominationList.toArray(new Denomination[denominationList.size()]);
    }



    /**
     * Validate Account and Amount to be withdrawn.
     * @param accountNo     Account No
     * @param amount        Amount to be withdrawn.
     * @throws AccountDoesNotExistException         Raised when Account No is not correct.
     * @throws InsufficientFundsException           Raised when Amount is greater then balance in the account or the amount does not exist in the ATM.
     * @throws IncorrectAmountWithdrawnException    Raised when Amount to be withdrawn is greater or less then limit specified.
     */
    private void validateAccountAndAmount(String accountNo, long amount) throws AccountDoesNotExistException,
            InsufficientFundsException, IncorrectAmountWithdrawnException {
        //Checking Bounds.
        if(amount < minimumWithdrawLimit) throw new IncorrectAmountWithdrawnException("Incorrect Amount Withdrawn Limit, should be between " + minimumWithdrawLimit + " and  " + maximumWithdrawLimit);
        if(amount > maximumWithdrawLimit) throw new IncorrectAmountWithdrawnException("Incorrect Amount Withdrawn Limit, should be between " + minimumWithdrawLimit + " and  " + maximumWithdrawLimit);
        if(amount % 5 != 0) throw new IncorrectAmountWithdrawnException("Amount to be withdrawn has to be in multiples of 5");

        Account account = accountService.getAccount(accountNo);
        //Check whether account has sufficient funds
        if(account.getBalance().longValue() < amount) throw new InsufficientFundsException("Insufficient balance in the account ");

        //Checking whether ATM has sufficient funds.
        if(getDenominationTotalAmount() < amount){
            throw new InsufficientFundsException("Currently ATM does not have sufficient funds, lower your requested amount.");
        }

        Denomination denomination5Note = mapOfDenomination.get(Notes.FIVE);
        if(amount % 10 != 0 && amount % 5==0 && denomination5Note.getCountOfNotes()==0){
            throw new IncorrectAmountWithdrawnException("Currently 5 Note is not present, please enter different amount multiples of 10");
        }

    }

    /**
     * Compute the Total of denomination total.
     * @return
     */
    private long getDenominationTotalAmount(){
        LongAdder amount = new LongAdder();
        denominationRepository.findAll().forEach(a -> amount.add(a.getAmount()));
        return amount.longValue();
    }
}
