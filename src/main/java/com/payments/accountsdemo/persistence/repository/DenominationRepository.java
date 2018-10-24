package com.payments.accountsdemo.persistence.repository;

import com.payments.accountsdemo.model.Notes;
import com.payments.accountsdemo.persistence.entities.Denomination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DenominationRepository extends CrudRepository<Denomination, Long> {

    /**
     * Finder in Denomination using Notes.
     * @param notes
     * @return
     */
    public Optional<Denomination> findByNotes(Notes notes);
}
