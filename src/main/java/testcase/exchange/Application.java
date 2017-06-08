package testcase.exchange;

import testcase.exchange.error.ExchangeException;

import java.math.BigInteger;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        printGreeting();
        Auction auction = new Auction();
        PossibleDeals deals = parseInput(auction);
        if(deals.getMaxAmount().equals(BigInteger.ZERO)) {
            System.out.println("0 n/a");
        } else {
            System.out.print(deals.getMaxAmount() + " " + deals.getOptimalPrice());
        }
    }

    private static void printGreeting() {
        System.out.println("Welcome to the basic exchange service!");
        System.out.println("Please place orders in a format <type> <amount> <price>. Types are S for sale and B for buy");
        System.out.println("For example:");
        System.out.println("S 100 15.42");
        System.out.println("B 20 17.02");
        System.out.println("Then type 'exchange', service will calculate optimal price");
    }

    private static PossibleDeals parseInput(Auction auction) {
        Scanner scan = new Scanner(System.in);
        String line;
        while ((line = scan.nextLine()) != null) {
            PossibleDeals deals = parseLine(line, auction);
            if(deals != null)
                return deals;
        }

        return null;
    }

    private static PossibleDeals parseLine(String line, Auction auction) {
        try {
            if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                System.exit(1);
            } else if(line.equalsIgnoreCase("exchange")) {
                return auction.exchange();
            } else if(line.matches("[Ss] \\d+ \\d+(.\\d+)?")) {
                auction.addSellOrder(parseOrder(line));
            } else if(line.matches("[Bb] \\d+ \\d+(.\\d+)?")) {
                auction.addBuyOrder(parseOrder(line));
            } else if(line.matches("\\s*")) {
                return null;
            } else {
                System.out.println("Can't parse, please try again");
            }
        } catch (ExchangeException e) {
            System.out.println(e.getMessage() + ", please try again");
        }

        return null;
    }

    private static Order parseOrder(String string) {
        String[] strings = string.split(" ");
        return new Order(strings[2], strings[1]);
    }
}
