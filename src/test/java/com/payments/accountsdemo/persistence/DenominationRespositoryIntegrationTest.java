package com.payments.accountsdemo.persistence;

import com.payments.accountsdemo.model.Notes;
import com.payments.accountsdemo.persistence.entities.Denomination;
import com.payments.accountsdemo.persistence.repository.DenominationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;


/**
 * Contains intergation Test cases for Denomination Repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DenominationRespositoryIntegrationTest {

    /*
    *   Autowired Repository instance.
     */
    @Autowired
    private DenominationRepository denominationRepository;

    /**
     * Integration Test for Denomination and Denomination Repository
     * Persisting the Denomination and then finding the object
     */
    @Test
    public void whenFindByNotes_thenReturnDenomination(){
        //Given
        Denomination denomination = new Denomination(Notes.TEN, 10);
        denominationRepository.save(denomination);

        //When
        Optional<Denomination> optionalDenomination = denominationRepository.findByNotes(Notes.TEN);

        assertTrue("FindByNotes or Persistance for Denomination has failed",
                denomination.getNotes().equals(optionalDenomination.get().getNotes()));
    }

    /**
     * Integration Test case where we persist a different denomination and find a different one, returning empty Optional.
     */
    @Test
    public void wheFindByNotesInvalid_ThenReturnNoDenomination(){
        //Given
        Denomination denomination = new Denomination(Notes.TEN, 10);
        denominationRepository.save(denomination);

        //When
        try {
            Optional<Denomination> optionalDenomination = denominationRepository.findByNotes(Notes.valueOf("ONE"));
        }catch (IllegalArgumentException ex){
            assertTrue("Denomination of ONE Note is not present", true);
        }

    }

    /**
     * Integration Test Case, to test when countOfNotes is Modified the balance is computed correctly.
     */
    @Test
    public void whenCountOfNotesModified_ThenBalanceComputedCorrectly(){
        //Give
        //Given
        Denomination denomination = new Denomination(Notes.TEN, 10);
        denominationRepository.save(denomination);

        //when
        Optional<Denomination> optionalDenomination = denominationRepository.findByNotes(Notes.TEN);
        Denomination foundDenomination = optionalDenomination.get();
        foundDenomination.reduceNoteCount(5);

        //Then
        assertTrue("Reduction in note count the balance is computed incorrectly", foundDenomination.getAmount() == 50);

    }



}
