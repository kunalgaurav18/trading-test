package com.jpmorgan.kunal.exceptions;

public class InvalidTransactionIDException extends RuntimeException {
    public InvalidTransactionIDException(String id) {
        super("The CANCEL request with transaction ID '" + id + "' is invalid. Please record it again.");
    }
}
