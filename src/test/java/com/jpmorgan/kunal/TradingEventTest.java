package com.jpmorgan.kunal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingEventTest {

    @Test
    void getKey() {
        TradingEvent tradingEvent = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        assertEquals("ACC1 SEC1", tradingEvent.getKey());
    }
}