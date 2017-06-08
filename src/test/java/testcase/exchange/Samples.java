package testcase.exchange;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class Samples {

    @Test
    public void firstSample() {
        DiscreteAuction auction = new DiscreteAuction();
        auction.addBuyOrder(new Order("10.00", "100"));
        auction.addSellOrder(new Order("10.10", "150"));
        assertFalse(auction.exchangePossible());
    }

    @Test
    public void secondSample() {
        DiscreteAuction auction = new DiscreteAuction();
        auction.addBuyOrder(new Order("15.40", "100"));
        auction.addBuyOrder(new Order("15.30", "100"));
        auction.addSellOrder(new Order("15.30", "150"));
        DealService dealService = auction.exchange();
        assertEquals(new BigDecimal("15.30"), dealService.getOptimalPrice());
        assertEquals(new BigInteger("150"), dealService.getMaxAmount());
    }

    @Test
    public void multipleOptimalPrices() {
        DiscreteAuction auction = new DiscreteAuction();
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
        DealService dealService = auction.exchange();
        assertEquals(new BigDecimal("35.00"), dealService.getOptimalPrice());
        assertEquals(new BigInteger("300"), dealService.getMaxAmount());
    }

    @Test
    public void over9000() {
        DiscreteAuction auction = new DiscreteAuction();
        for(int i = 0; i < DiscreteAuction.ORDERS_LIMIT;) {
            BigDecimal price = BigDecimal.ONE;
            BigDecimal inc = new BigDecimal("0.01");
            String amount = "1000";
            for(int j = 0; j < 9900 && i < DiscreteAuction.ORDERS_LIMIT; j++,i+=2) {
                price = price.add(inc);
                auction.addBuyOrder(new Order(price.toString(), amount));
                auction.addSellOrder(new Order(price.toString(), amount));
            }
        }
        DealService dealService = auction.exchange();
        assertEquals(new BigDecimal("50.02"), dealService.getOptimalPrice());
        assertEquals(new BigInteger("250002000"), dealService.getMaxAmount());
    }
}
