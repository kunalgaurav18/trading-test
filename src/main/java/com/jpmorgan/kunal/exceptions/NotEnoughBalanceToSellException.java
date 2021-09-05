package com.jpmorgan.kunal.exceptions;

public class NotEnoughBalanceToSellException extends RuntimeException {
    public NotEnoughBalanceToSellException(Long id, Integer balance) {
        super("Not enough balance to execute the transaction id '" + id + "' and total available balance is " + balance);
    }
}
