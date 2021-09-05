package com.jpmorgan.kunal;

import com.jpmorgan.kunal.exceptions.InvalidTransactionIDException;
import com.jpmorgan.kunal.exceptions.NoRecordForBuying;
import com.jpmorgan.kunal.exceptions.NotEnoughBalanceToSellException;
import com.jpmorgan.kunal.exceptions.RepeatedIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PositionBookTest {

    private PositionBook positionBook;

    @BeforeEach
    void setup() {
        positionBook = new PositionBook();
    }

    @Test
    @DisplayName("Test displayBook when position book is empty")
    void displayBookWhenEmpty() {
        String result = positionBook.displayBook();
        assertEquals("No data.", result);
    }

    @Test
    @DisplayName("Adding single event")
    void addBuyEvent() {
        TradingEvent event = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        Map<String, List<TradingEvent>> book = positionBook.addEvent(event);
        assertFalse(book.isEmpty());
        assertTrue(book.containsKey(event.getKey()));
    }

    @Test
    @DisplayName("Adding multiple event")
    void addBuyEvents() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC1", "SEC1", 100);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        Map<String, List<TradingEvent>> book = positionBook.addEvent(event3);
        assertFalse(book.isEmpty());
        assertTrue(book.containsKey(event1.getKey()));
        assertEquals(3, book.get(event1.getKey()).size());
    }

    @Test
    @DisplayName("Adding multiple events with different security")
    void addBuyEventsDiffSecurity() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC2", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC1", "SEC3", 100);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        Map<String, List<TradingEvent>> book = positionBook.addEvent(event3);
        assertFalse(book.isEmpty());
        assertTrue(book.containsKey(event1.getKey()));
        assertTrue(book.containsKey(event2.getKey()));
        assertTrue(book.containsKey(event3.getKey()));
        assertEquals(1, book.get(event1.getKey()).size());
        assertEquals(1, book.get(event2.getKey()).size());
        assertEquals(1, book.get(event3.getKey()).size());
    }

    @Test
    @DisplayName("Adding multiple events with different accounts")
    void addBuyEventsDiffAccount() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC2", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC3", "SEC1", 100);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        Map<String, List<TradingEvent>> book = positionBook.addEvent(event3);
        assertFalse(book.isEmpty());
        assertTrue(book.containsKey(event1.getKey()));
        assertTrue(book.containsKey(event2.getKey()));
        assertTrue(book.containsKey(event3.getKey()));
        assertEquals(1, book.get(event1.getKey()).size());
        assertEquals(1, book.get(event2.getKey()).size());
        assertEquals(1, book.get(event3.getKey()).size());
    }

    @Test
    @DisplayName("Adding events with existing ID having same account and same security")
    void addEventsWithExistingIDCase1() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }


    @Test
    @DisplayName("Adding events with existing ID having same account and different security")
    void addEventsWithExistingIDCase2() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC2", 100);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding events with existing ID having different account and same security")
    void addEventsWithExistingIDCase3() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.BUY, "ACC2", "SEC1", 100);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding events with existing ID having different account and different security")
    void addEventsWithExistingIDCase4() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.BUY, "ACC2", "SEC2", 100);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding sell events with existing ID having different account and different security")
    void addEventsWithExistingIDCase5() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.BUY, "ACC2", "SEC2", 100);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding sell event with existing ID")
    void addSellEventWithExistingID() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(1L, Actions.SELL, "ACC1", "SEC1", 50);
        positionBook.addEvent(event1);
        assertThrows(RepeatedIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding sell event having quantity greater than balance")
    void addSellEventWithQuantityGreaterThanBalance() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.SELL, "ACC1", "SEC1", 150);
        positionBook.addEvent(event1);
        assertThrows(NotEnoughBalanceToSellException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding sell event for account without buying records")
    void addSellEventForNoBuyingRecords() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.SELL, "ACC2", "SEC1", 150);
        positionBook.addEvent(event1);
        assertThrows(NoRecordForBuying.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Adding cancel event for not existing ID")
    void addingCancelEventForNonExistentID() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.CANCEL, "ACC1", "SEC1", 0);
        positionBook.addEvent(event1);
        assertThrows(InvalidTransactionIDException.class, () -> positionBook.addEvent(event2));
    }

    @Test
    @DisplayName("Checking balance after buying")
    void checkingBalanceAfterBuying() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        assertTrue(positionBook.displayBook().contains("ACC1 SEC1 200"));
    }

    @Test
    @DisplayName("Checking balance after buying multiple accounts and security")
    void checkingBalanceAfterBuyingMultiAccSec() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC2", "SEC3", 100);
        TradingEvent event4 = new TradingEvent(4L, Actions.BUY, "ACC3", "SEC2", 100);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        positionBook.addEvent(event3);
        positionBook.addEvent(event4);
        assertTrue(positionBook.displayBook().contains("ACC1 SEC1 200"));
        assertTrue(positionBook.displayBook().contains("ACC2 SEC3 100"));
        assertTrue(positionBook.displayBook().contains("ACC3 SEC2 100"));
    }

    @Test
    @DisplayName("Checking balance after buying and selling multiple accounts and security")
    void checkingBalanceAfterSellingMultiAccSec() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC2", "SEC3", 100);
        TradingEvent event4 = new TradingEvent(4L, Actions.BUY, "ACC3", "SEC2", 100);
        TradingEvent event5 = new TradingEvent(5L, Actions.SELL, "ACC1", "SEC1", 50);
        TradingEvent event6 = new TradingEvent(6L, Actions.SELL, "ACC3", "SEC2", 50);
        TradingEvent event7 = new TradingEvent(7L, Actions.SELL, "ACC2", "SEC3", 50);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        positionBook.addEvent(event3);
        positionBook.addEvent(event4);
        positionBook.addEvent(event5);
        positionBook.addEvent(event6);
        positionBook.addEvent(event7);
        assertTrue(positionBook.displayBook().contains("ACC1 SEC1 150"));
        assertTrue(positionBook.displayBook().contains("ACC2 SEC3 50"));
        assertTrue(positionBook.displayBook().contains("ACC3 SEC2 50"));
    }

    @Test
    @DisplayName("Checking balance after cancelling a buy event multiple accounts and security")
    void checkingBalanceAfterCancellingBuyMultiAccSec() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC2", "SEC3", 100);
        TradingEvent event4 = new TradingEvent(4L, Actions.BUY, "ACC3", "SEC2", 100);
        TradingEvent event5 = new TradingEvent(5L, Actions.SELL, "ACC1", "SEC1", 50);
        TradingEvent event6 = new TradingEvent(6L, Actions.SELL, "ACC3", "SEC2", 50);
        TradingEvent event7 = new TradingEvent(7L, Actions.SELL, "ACC2", "SEC3", 50);
        TradingEvent event8 = new TradingEvent(2L, Actions.CANCEL, "ACC1", "SEC1", 0);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        positionBook.addEvent(event3);
        positionBook.addEvent(event4);
        positionBook.addEvent(event5);
        positionBook.addEvent(event6);
        positionBook.addEvent(event7);
        positionBook.addEvent(event8);
        assertTrue(positionBook.displayBook().contains("ACC1 SEC1 50"));
        assertTrue(positionBook.displayBook().contains("ACC2 SEC3 50"));
        assertTrue(positionBook.displayBook().contains("ACC3 SEC2 50"));
    }

    @Test
    @DisplayName("Checking balance after cancelling a sell event multiple accounts and security")
    void checkingBalanceAfterCancellingSellMultiAccSec() {
        TradingEvent event1 = new TradingEvent(1L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event2 = new TradingEvent(2L, Actions.BUY, "ACC1", "SEC1", 100);
        TradingEvent event3 = new TradingEvent(3L, Actions.BUY, "ACC2", "SEC3", 100);
        TradingEvent event4 = new TradingEvent(4L, Actions.BUY, "ACC3", "SEC2", 100);
        TradingEvent event5 = new TradingEvent(5L, Actions.SELL, "ACC1", "SEC1", 50);
        TradingEvent event6 = new TradingEvent(6L, Actions.SELL, "ACC3", "SEC2", 50);
        TradingEvent event7 = new TradingEvent(7L, Actions.SELL, "ACC2", "SEC3", 50);
        TradingEvent event8 = new TradingEvent(6L, Actions.CANCEL, "ACC3", "SEC2", 0);
        positionBook.addEvent(event1);
        positionBook.addEvent(event2);
        positionBook.addEvent(event3);
        positionBook.addEvent(event4);
        positionBook.addEvent(event5);
        positionBook.addEvent(event6);
        positionBook.addEvent(event7);
        positionBook.addEvent(event8);
        assertTrue(positionBook.displayBook().contains("ACC1 SEC1 150"));
        assertTrue(positionBook.displayBook().contains("ACC2 SEC3 50"));
        assertTrue(positionBook.displayBook().contains("ACC3 SEC2 100"));
    }

}