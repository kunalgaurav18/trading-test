package com.jpmorgan.kunal.exceptions;

public class NoRecordForBuying extends RuntimeException {
    public NoRecordForBuying(String account, String security) {
        super("No buying record found for the given account and security. '" + account + "' '" + security + "'");
    }
}
