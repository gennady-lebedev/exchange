package testcase.exchange;

import java.math.BigInteger;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        printGreeting();
        DiscreteAuction auction = new DiscreteAuction();
        DealService deals = parseInput(auction);
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

    private static DealService parseInput(DiscreteAuction auction) {
        Scanner scan = new Scanner(System.in);
        String line;
        while ((line = scan.nextLine()) != null) {
            DealService deals = parseLine(line, auction);
            if(deals != null)
                return deals;
        }

        return null;
    }

    private static DealService parseLine(String line, DiscreteAuction auction) {
        if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
            System.exit(1);
        } else if(line.equalsIgnoreCase("exchange")) {
            return auction.exchange();
        } else if(line.matches("[Ss] \\d+ \\d+(.\\d+)?")) {
            auction.addSellOrder(parseOrder(line));
        } else if(line.matches("[Bb] \\d+ \\d+(.\\d+)?")) {
            auction.addBuyOrder(parseOrder(line));
        } else {
            System.out.println("Can't parse, please try again");
        }
        return null;
    }

    private static Order parseOrder(String string) {
        String[] strings = string.split(" ");
        return new Order(strings[2], strings[1]);
    }
}
