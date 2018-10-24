package com.payments.accountsdemo.persistence.entities;

import com.payments.accountsdemo.model.Notes;

import javax.persistence.*;
import java.util.Objects;

/**
 * Denomination, stores the denomination of notes, count of notes, total amount.
 * Creating it final and immutable helps as it helps
 */
@Entity
@Table(name = "DENOMINATION")
public final class Denomination {

    @Id
    @Column (name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "NOTES")
    @Enumerated (EnumType.STRING)
    private Notes notes;

    @Column (name = "COUNT")
    private int countOfNotes;

    @Column (name = "AMOUNT")
    private long amount;

    /**
     * Default Constructor.
     */
    public Denomination(){

    }

    /**
     * Constructor for Denomination Object, depicting
     * @param notes
     * @param countOfNotes
     */
    public Denomination(Notes notes, int countOfNotes){
        this.notes = notes;
        this.countOfNotes = countOfNotes;
        this.amount = computeAmount(notes, countOfNotes);
    }

    /**
     * Getter for Notes
     * @return
     */
    public Notes getNotes(){
        return notes;
    }

    /**
     * Getter for count of Notes
     * @return
     */
    public int getCountOfNotes(){
        return countOfNotes;
    }

    /**
     * Getter for Amount.
     * @return
     */
    public long getAmount(){
        return amount;
    }

    /**
     * Equals Implementation.s
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return countOfNotes == that.countOfNotes &&
                notes == that.notes &&
                Objects.equals(amount, that.amount);
    }

    /**
     * Hashcode implementation
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(notes, countOfNotes, amount);
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "notes=" + notes +
                ", countOfNotes=" + countOfNotes +
                ", amount=" + amount +
                '}';
    }

    private long computeAmount(Notes notes, int counfOfNotes){
        if(notes!=null  && counfOfNotes > 0){
            return notes.getValue() * counfOfNotes;
        }
        return 0;
    }

    /**
     * Reset would, reset the count to default specified in the application properties.
     */
    public void reset(int defaultSize){
        this.countOfNotes = defaultSize;
        this.amount = computeAmount(notes, countOfNotes);
    }

    /**
     * Reduction of Note Count, when we take it from the denomination
     * @param count Count by which to reduce.
     */
    public void reduceNoteCount(int count){
        if(count > countOfNotes) throw new IllegalArgumentException("Number of Notes for denomination "+ Notes.FIFTY.toString() + " is not present");
        this.countOfNotes -= count;
        this.amount = computeAmount(notes, countOfNotes);
    }
}
