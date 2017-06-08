package testcase.exchange;

import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testcase.exchange.error.ExchangeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
@ToString
public class Cumulative {
    private static final Logger log = LoggerFactory.getLogger(Cumulative.class);

    private final NavigableMap<BigDecimal, BigInteger> buyCumulative;
    private final NavigableMap<BigDecimal, BigInteger> sellCumulative;

    public Cumulative(NavigableMap<BigDecimal, BigInteger> buyOrders,
                      NavigableMap<BigDecimal, BigInteger> sellOrders) {
        buyCumulative = accumulateBuyOrders(buyOrders);
        sellCumulative = accumulateSellOrders(sellOrders);

        log.trace("Sell orders grouped by price: {}", sellOrders);
        log.trace("Sell cumulative: {}", sellCumulative);

        log.trace("Buy orders grouped by price: {}", buyOrders);
        log.trace("Buy cumulative: {}", buyCumulative);
    }

    private NavigableMap<BigDecimal, BigInteger> accumulateBuyOrders(NavigableMap<BigDecimal, BigInteger> buyOrders) {
        if(buyOrders.size() == 0) {
            throw new ExchangeException("No buy orders to accumulate");
        }
        NavigableMap<BigDecimal, BigInteger> buyCumulative = new TreeMap<>();
        for(BigDecimal price: buyOrders.descendingKeySet()) {
            BigInteger amount = buyOrders.get(price);
            BigDecimal tailPrice = buyOrders.higherKey(price);
            if(tailPrice != null) {
                BigInteger tailAmount = buyCumulative.get(tailPrice);
                buyCumulative.put(price, amount.add(tailAmount));
            } else {
                buyCumulative.put(price, amount);
            }
        }
        return buyCumulative;
    }

    private NavigableMap<BigDecimal, BigInteger> accumulateSellOrders(NavigableMap<BigDecimal, BigInteger> sellOrders) {
        if(sellOrders.size() == 0) {
            throw new ExchangeException("No sell orders to accumulate");
        }
        NavigableMap<BigDecimal, BigInteger> sellCumulative = new TreeMap<>();
        for(BigDecimal price: sellOrders.navigableKeySet()) {
            BigInteger amount = sellOrders.get(price);
            BigDecimal headPrice = sellOrders.lowerKey(price);
            if(headPrice != null) {
                BigInteger headAmount = sellCumulative.get(headPrice);
                sellCumulative.put(price, amount.add(headAmount));
            } else {
                sellCumulative.put(price, amount);
            }
        }
        return sellCumulative;
    }
}
