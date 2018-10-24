package com.payments.accountsdemo.model;

/**
 * Enum providing list of all the notes denomination.
 */
public enum Notes {

    FIFTY("Fifty", 50),
    TWENTY("Twenty", 20),
    TEN("Ten", 10),
    FIVE("Five", 5);

    private String name;
    private int value;

    Notes(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public int getValue(){
        return value;
    }

    //Need to implement valueOf etc..

    @Override
    public String toString() {
        return "Notes{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
