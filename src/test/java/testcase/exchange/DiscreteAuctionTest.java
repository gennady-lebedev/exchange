package testcase.exchange;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import testcase.exchange.error.OrderLimitException;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class DiscreteAuctionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DiscreteAuction auction;

    @Before
    public void before() {
        auction = new DiscreteAuction();
    }

    @Test
    public void exchangePossibleWhenPricesAreOk() {
        auction.addSellOrder(new Order("10.00", "10"));
        auction.addBuyOrder(new Order("25.00", "20"));
        assertTrue("Exchange failed with correct prices", auction.exchangePossible());
    }

    @Test
    public void exchangeNotPossibleWhenPricesAreNotOk() {
        auction.addSellOrder(new Order("25.00", "10"));
        auction.addBuyOrder(new Order("10.00", "20"));
        assertFalse("Exchange been made with incorrect prices", auction.exchangePossible());
    }

    @Test
    public void verySimpleExchange() throws Exception {
        Order order = new Order("10.00", "100");
        auction.addSellOrder(order);
        auction.addBuyOrder(order);
        DealService deals = auction.exchange();
        DealService mock = new DealService();
        mock.rememberDeal(new BigDecimal("10.00"), new BigDecimal("10.00"), new BigInteger("100"));
        mock.findOptimalPrice();
        assertEquals("Same sell and buy orders should meet a deal", mock, deals);
    }

    @Test
    public void buy2OrdersWithDifferentPrice() throws Exception {
        auction.addSellOrder(new Order("10.00", "10"));
        auction.addSellOrder(new Order("20.00", "10"));

        auction.addBuyOrder(new Order("25.00", "15"));
        auction.addBuyOrder(new Order("30.00", "5"));
        DealService deals = auction.exchange();

        assertEquals("Optimal price calculated incorrect", new BigDecimal("22.50"), deals.getOptimalPrice());
        assertEquals("Maximum amount incorrect", new BigInteger("20"), deals.getMaxAmount());
    }

    @Test
    public void totalAmountOfOrdersCouldBeLessThanLimit() {
        for(int i = 0; i < DiscreteAuction.ORDERS_LIMIT; i++) {
            auction.addBuyOrder(new Order("1", "1"));
        }
    }

    @Test
    public void totalAmountOfOrdersShouldNotBeMoreThanLimit() {
        thrown.expect(OrderLimitException.class);
        for(int i = 0; i < DiscreteAuction.ORDERS_LIMIT; i++) {
            auction.addBuyOrder(new Order("1", "1"));
        }
        auction.addSellOrder(new Order("1", "1"));
    }
}