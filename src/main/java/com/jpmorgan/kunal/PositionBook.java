package com.jpmorgan.kunal;

import com.jpmorgan.kunal.exceptions.InvalidTransactionIDException;
import com.jpmorgan.kunal.exceptions.NoRecordForBuying;
import com.jpmorgan.kunal.exceptions.NotEnoughBalanceToSellException;
import com.jpmorgan.kunal.exceptions.RepeatedIDException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PositionBook {
    private final Map<String, List<TradingEvent>> tradingEvents = new HashMap<>();

    public Map<String, List<TradingEvent>> addEvent(TradingEvent event) {
        validateEvent(event);

        if (tradingEvents.containsKey(event.getKey())) {
            tradingEvents.get(event.getKey()).add(event);
        } else {
            List<TradingEvent> tempList = new ArrayList<>();
            tempList.add(event);
            tradingEvents.put(event.getKey(), tempList);
        }
        return tradingEvents;
    }

    private boolean isIdPresent(TradingEvent event) {
        if (tradingEvents.isEmpty()) return false;
        Long id = event.getId();
        return tradingEvents.values().stream().flatMap(List::stream).anyMatch(e -> e.getId().equals(id));
    }

    public String displayBook() {
        if (tradingEvents.isEmpty()) {
            return "No data.";
        }
        return
                tradingEvents.keySet().stream().map(e -> e + " " + this.getBalance(e) + "\n\t" +
                        tradingEvents.get(e).stream().map(TradingEvent::toString)
                                .collect(Collectors.joining("\n\t")) + "\n").collect(Collectors.joining());

    }

    private Integer getBalance(String key) {
        List<Long> cancelledEvents = this.getCancelledEvents(key);
        return tradingEvents.get(key).stream().mapToInt(e -> {
            if (e.getAction() == Actions.BUY && !cancelledEvents.contains(e.getId())) {
                return e.getQuantity();
            } else {
                if (e.getAction() == Actions.SELL && !cancelledEvents.contains(e.getId())) return e.getQuantity() * -1;
                return 0;
            }
        }).sum();
    }

    private List<Long> getCancelledEvents(String key) {
        return tradingEvents.get(key).stream().filter(e -> e.getAction() == Actions.CANCEL).map(TradingEvent::getId)
                .collect(Collectors.toList());
    }

    private void validateEvent(TradingEvent event) {
        if (event.getAction() == Actions.BUY && isIdPresent(event)) {
            throw new RepeatedIDException(event.getId());
        }

        if (event.getAction() == Actions.CANCEL && !isIdPresent(event)) {
            throw new InvalidTransactionIDException(event.getId().toString());
        }

        if (event.getAction() == Actions.SELL) {
            if (isIdPresent(event)) {
                throw new RepeatedIDException(event.getId());
            }
            if (tradingEvents.isEmpty() || !tradingEvents.containsKey(event.getKey())) {
                throw new NoRecordForBuying(event.getAccount(), event.getSecurity());
            }
            if (getBalance(event.getKey()) < event.getQuantity()) {
                throw new NotEnoughBalanceToSellException(event.getId(), getBalance(event.getKey()));
            }
        }
    }
}
