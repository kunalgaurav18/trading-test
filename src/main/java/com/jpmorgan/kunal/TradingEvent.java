package com.jpmorgan.kunal;

public class TradingEvent {

    private Long id;
    private Actions action;
    private String account;
    private String security;
    private Integer quantity;

    public TradingEvent(Long id, Actions action, String account, String security, Integer quantity) {
        this.id = id;
        this.action = action;
        this.account = account;
        this.security = security;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Actions getAction() {
        return action;
    }

    public String getAccount() {
        return account;
    }

    public String getSecurity() {
        return security;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "[id: " + this.id + ", " + this.getAction() + ", " + this.getAccount() + ", " + this.getSecurity() +
                ", " + this.getQuantity() + "]";
    }

    public String getKey() {
        return this.getAccount() + " " + this.getSecurity();
    }
}
