package testcase.exchange;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class Samples {

    @Test
    public void firstSample() {
        Auction auction = new Auction();
        auction.addBuyOrder(new Order("10.00", "100"));
        auction.addSellOrder(new Order("10.10", "150"));
        assertFalse(auction.exchangePossible());
    }

    @Test
    public void secondSample() {
        Auction auction = new Auction();
        auction.addBuyOrder(new Order("15.40", "100"));
        auction.addBuyOrder(new Order("15.30", "100"));
        auction.addSellOrder(new Order("15.30", "150"));
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("15.30"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("150"), possibleDeals.getMaxAmount());
    }

    @Test
    public void multipleOptimalPrices() {
        Auction auction = new Auction();
        auction.addBuyOrder(new Order("10", "100"));
        auction.addBuyOrder(new Order("20", "100"));
        auction.addBuyOrder(new Order("30", "100"));
        auction.addBuyOrder(new Order("40", "100"));
        auction.addBuyOrder(new Order("50", "100"));
        auction.addBuyOrder(new Order("60", "100"));
        auction.addSellOrder(new Order("10", "100"));
        auction.addSellOrder(new Order("20", "100"));
        auction.addSellOrder(new Order("30", "100"));
        auction.addSellOrder(new Order("40", "100"));
        auction.addSellOrder(new Order("50", "100"));
        auction.addSellOrder(new Order("60", "100"));
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("35.00"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("300"), possibleDeals.getMaxAmount());
    }

    @Test
    public void ridiculousPricesAndAmounts() {
        Auction auction = new Auction();
        auction.addBuyOrder(new Order("1", "1000"));
        auction.addBuyOrder(new Order("10", "1"));
        auction.addBuyOrder(new Order("100", "1000"));
        auction.addSellOrder(new Order("1", "1000"));
        auction.addSellOrder(new Order("10", "1"));
        auction.addSellOrder(new Order("100", "1000"));
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("10.00"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("1001"), possibleDeals.getMaxAmount());
    }

    @Test
    public void crossedRangesOfPrices() {
        Auction auction = new Auction();
        auction.addBuyOrder(new Order("1", "100"));
        auction.addBuyOrder(new Order("10", "100"));
        auction.addBuyOrder(new Order("20", "100"));
        auction.addBuyOrder(new Order("50", "100"));

        auction.addSellOrder(new Order("20", "100"));
        auction.addSellOrder(new Order("50", "100"));
        auction.addSellOrder(new Order("80", "100"));
        auction.addSellOrder(new Order("100", "100"));
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("35.00"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("100"), possibleDeals.getMaxAmount());
    }

    @Test
    public void crossedRangesOfPrices2() {
        Auction auction = new Auction();
        auction.addSellOrder(new Order("1", "100"));
        auction.addSellOrder(new Order("10", "100"));
        auction.addSellOrder(new Order("20", "100"));
        auction.addSellOrder(new Order("50", "100"));

        auction.addBuyOrder(new Order("20", "100"));
        auction.addBuyOrder(new Order("50", "100"));
        auction.addBuyOrder(new Order("80", "100"));
        auction.addBuyOrder(new Order("100", "100"));
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("35.00"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("300"), possibleDeals.getMaxAmount());
    }


    @Test
    public void over9000() {
        Auction auction = new Auction();
        for(int i = 0; i < Auction.ORDERS_LIMIT;) {
            BigDecimal price = BigDecimal.ONE;
            BigDecimal inc = new BigDecimal("0.01");
            String amount = "1000";
            for(int j = 0; j < 9900 && i < Auction.ORDERS_LIMIT; j++,i+=2) {
                price = price.add(inc);
                auction.addBuyOrder(new Order(price.toString(), amount));
                auction.addSellOrder(new Order(price.toString(), amount));
            }
        }
        PossibleDeals possibleDeals = auction.exchange();
        assertEquals(new BigDecimal("50.02"), possibleDeals.getOptimalPrice());
        assertEquals(new BigInteger("250002000"), possibleDeals.getMaxAmount());
    }
}
