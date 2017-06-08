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
}
