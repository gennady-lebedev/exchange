package testcase.exchange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class CumulativeSpec {
    private static final Logger log = LoggerFactory.getLogger(CumulativeSpec.class);
    private NavigableMap<BigDecimal, BigInteger> buyOrders;
    private NavigableMap<BigDecimal, BigInteger> sellOrders;

    @Before
    public void before() {
        buyOrders = new TreeMap<>();
        sellOrders = new TreeMap<>();
    }

    @Test
    public void oneOrderBecameOneCumulativeAndDoesNotChangeValues() {
        buyOrders.put(new BigDecimal("1.00"), new BigInteger("100"));
        sellOrders.put(new BigDecimal("1.00"), new BigInteger("100"));
        Cumulative cumulative = new Cumulative(buyOrders, sellOrders);
        assertEquals(new BigInteger("100"), cumulative.getBuyCumulative().get(new BigDecimal("1.00")));
        assertEquals(new BigInteger("100"), cumulative.getSellCumulative().get(new BigDecimal("1.00")));
    }

    @Test
    public void amountStacksDifferentForBuyAndSell () {
        buyOrders.put(new BigDecimal("10.00"), new BigInteger("100"));
        buyOrders.put(new BigDecimal("20.00"), new BigInteger("100"));
        buyOrders.put(new BigDecimal("30.00"), new BigInteger("100"));
        sellOrders.put(new BigDecimal("10.00"), new BigInteger("100"));
        sellOrders.put(new BigDecimal("20.00"), new BigInteger("100"));
        sellOrders.put(new BigDecimal("30.00"), new BigInteger("100"));
        Cumulative cumulative = new Cumulative(buyOrders, sellOrders);
        assertEquals(new BigInteger("300"), cumulative.getBuyCumulative().get(new BigDecimal("10.00")));
        assertEquals(new BigInteger("200"), cumulative.getBuyCumulative().get(new BigDecimal("20.00")));
        assertEquals(new BigInteger("100"), cumulative.getBuyCumulative().get(new BigDecimal("30.00")));
        assertEquals(new BigInteger("100"), cumulative.getSellCumulative().get(new BigDecimal("10.00")));
        assertEquals(new BigInteger("200"), cumulative.getSellCumulative().get(new BigDecimal("20.00")));
        assertEquals(new BigInteger("300"), cumulative.getSellCumulative().get(new BigDecimal("30.00")));
    }

    @Test
    public void monkeyTestOfCumulative() {
        for(int i = 0; i < Auction.ORDERS_LIMIT;) {
            BigDecimal price = BigDecimal.ONE;
            BigDecimal inc = new BigDecimal("0.01");
            BigInteger amount = new BigInteger("100");
            for(int j = 0; j < 9900 && i < Auction.ORDERS_LIMIT; j++,i+=2) {
                price = price.add(inc);
                buyOrders.put(price, amount);
                sellOrders.put(price, amount);
            }
        }

        Cumulative cumulative = new Cumulative(buyOrders, sellOrders);
        NavigableMap<BigDecimal, BigInteger> buy = cumulative.getBuyCumulative();
        BigInteger prevAmount = buy.firstEntry().getValue();
        for (BigDecimal price: buy.navigableKeySet()) {
            assertTrue(buy.get(price).compareTo(prevAmount) <= 0);
            prevAmount = buy.get(price);
        }
        NavigableMap<BigDecimal, BigInteger> sell = cumulative.getSellCumulative();
        prevAmount = sell.firstEntry().getValue();
        for (BigDecimal price: sell.navigableKeySet()) {
            assertTrue(sell.get(price).compareTo(prevAmount) >= 0);
            prevAmount = sell.get(price);
        }
    }
}
