package com.jpmorgan.kunal.exceptions;

public class RepeatedIDException extends RuntimeException {
    public RepeatedIDException(Long id) {
        super("Another transaction with the same ID '"+id+"' exists. Please try again.");
    }
}
