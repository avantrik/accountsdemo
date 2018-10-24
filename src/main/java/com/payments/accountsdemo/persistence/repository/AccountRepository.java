package com.payments.accountsdemo.persistence.repository;

import com.payments.accountsdemo.persistence.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

}
