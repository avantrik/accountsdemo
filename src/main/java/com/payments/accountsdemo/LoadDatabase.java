package com.payments.accountsdemo;

import com.payments.accountsdemo.model.Notes;
import com.payments.accountsdemo.persistence.entities.Account;
import com.payments.accountsdemo.persistence.entities.Denomination;
import com.payments.accountsdemo.persistence.repository.AccountRepository;
import com.payments.accountsdemo.persistence.repository.DenominationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class LoadDatabase {

    private static Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Value("${payments.denomination.size}")
    private int defaultDenominationSize;

    @Bean
    CommandLineRunner initDatabase(AccountRepository accountRepository, DenominationRepository denominationRepository){

        return args -> {
            LOG.info("Preloading "+ accountRepository.save(new Account("01001", new BigDecimal("2738.59"))));
            LOG.info("Preloading "+ accountRepository.save(new Account("01002", new BigDecimal("23.00"))));
            LOG.info("Preloading "+ accountRepository.save(new Account("01003", new BigDecimal("0.00"))));

            LOG.info("Preloading "+ denominationRepository.save(new Denomination(Notes.FIFTY, defaultDenominationSize)));
            LOG.info("Preloading "+ denominationRepository.save(new Denomination(Notes.TWENTY, defaultDenominationSize)));
            LOG.info("Preloading "+ denominationRepository.save(new Denomination(Notes.TEN, defaultDenominationSize)));
            LOG.info("Preloading "+ denominationRepository.save(new Denomination(Notes.FIVE, defaultDenominationSize)));
        };

    }



}
