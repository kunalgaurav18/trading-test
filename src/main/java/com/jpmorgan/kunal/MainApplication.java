package com.jpmorgan.kunal;

import com.jpmorgan.kunal.exceptions.*;

import java.util.Scanner;

public class MainApplication {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String input = "continue";
        PositionBook positionBook = new PositionBook();

        while (!input.equalsIgnoreCase("bye")) {
            System.out.println("----- MENU ----------");
            System.out.println("1. Enter transaction.");
            System.out.println("2. Show position book.");
            System.out.println("Type 'BYE' to exit.");
            input = scanner.next();

            switch (input.toUpperCase()) {
                case "1":
                    doTransaction(positionBook);
                    break;
                case "2":
                    showPositionBook(positionBook);
                    break;
                case "BYE":
                    System.out.println("Exiting...!!!");
                    continue;
                default:
                    System.out.println("Try again!");
            }
        }

    }

    private static void doTransaction(PositionBook positionBook) {
        System.out.println("Please enter the transaction details in the format");
        System.out.println("<id> <action> <account> <security> <quantity>");
        System.out.println("Type 'DONE' when all transactions are added.");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("done")) {
            input = scanner.nextLine();
            if(input.split(" ").length == 5){
                TradingEvent event = null;
                try {
                    event = getEventFromString(input);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                try {
                    positionBook.addEvent(event);
                } catch (InvalidTransactionIDException | NotEnoughBalanceToSellException |
                        InvalidActionException | RepeatedIDException | NoRecordForBuying e) {
                    System.out.println(e.getMessage());
                }
            }
            else if(!input.equalsIgnoreCase("done")){
                System.out.println("Invalid input. Try again.");
            }

        }
    }

    private static void showPositionBook(PositionBook positionBook) {
        System.out.println(positionBook.displayBook());
    }

    private static TradingEvent getEventFromString(String input) {
        Long id = Long.valueOf(input.split(" ")[0]);
        Actions action = Actions.BUY;
        if(input.split(" ")[1].equalsIgnoreCase("buy") ||
                input.split(" ")[1].equalsIgnoreCase("sell") ||
                input.split(" ")[1].equalsIgnoreCase("cancel")) {
            switch (input.split(" ")[1].toUpperCase()) {
                case "SELL":
                    action = Actions.SELL;
                    break;
                case "CANCEL":
                    action = Actions.CANCEL;
                    break;
            }
        }
        else {
            throw new UnknownAction(input.split(" ")[1]);
        }
        String account = input.split(" ")[2];
        if(account.isBlank()) throw new BlankNotAllowed("Account cannot be blank");
        String security = input.split(" ")[3];
        if(security.isBlank()) throw new BlankNotAllowed("Security cannot be blank");
        Integer quantity = Integer.valueOf(input.split(" ")[4]);
        if(quantity.compareTo(0) < 0) throw new IllegalArgumentException("Quantity cannot be negative.");

        return new TradingEvent(id, action, account, security, quantity);
    }
}
