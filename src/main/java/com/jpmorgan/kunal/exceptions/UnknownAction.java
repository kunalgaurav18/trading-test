package com.jpmorgan.kunal.exceptions;

public class UnknownAction extends RuntimeException {
    public UnknownAction(String action) {
        super("The action '"+action+"' is unknown. Possible values for action are: BUY, SELL or CANCEL (ignore case).");
    }
}
