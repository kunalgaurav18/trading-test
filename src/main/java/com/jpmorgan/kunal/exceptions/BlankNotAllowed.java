package com.jpmorgan.kunal.exceptions;

public class BlankNotAllowed extends RuntimeException {
    public BlankNotAllowed(String message) {
        super(message);
    }
}
