package com.jpmorgan.kunal.exceptions;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException(Long id) {
        super("Cannot perform the transaction with ID '" + id + "'. No previous action found for this account and security.");
    }
}
