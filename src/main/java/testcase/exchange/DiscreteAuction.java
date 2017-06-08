package testcase.exchange;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testcase.exchange.error.OrderLimitException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
public class DiscreteAuction {
    private static final Logger log = LoggerFactory.getLogger(DiscreteAuction.class);

    public static final int ORDERS_LIMIT = 1000000;

    private final NavigableMap<BigDecimal, BigInteger> buyOrders;
    private final NavigableMap<BigDecimal, BigInteger> buyCumulative;
    private final NavigableMap<BigDecimal, BigInteger> sellOrders;
    private final NavigableMap<BigDecimal, BigInteger> sellCumulative;

    private int total = 0;


    public DiscreteAuction() {
        this.buyOrders = new TreeMap<>();
        this.buyCumulative = new TreeMap<>();
        this.sellOrders = new TreeMap<>();
        this.sellCumulative = new TreeMap<>();
    }

    public void addSellOrder(Order order) {
        addOrder(order, sellOrders);
    }

    public void addBuyOrder(Order order) {
        addOrder(order, buyOrders);
    }

    private void addOrder(Order order, NavigableMap<BigDecimal, BigInteger> orders) {
        checkAndCount();

        BigDecimal price = order.getPrice();
        BigInteger amount = order.getAmount();

        if(orders.containsKey(price)) {
            amount = amount.add(orders.get(price));
        }
        orders.put(price, amount);
    }

    private void checkAndCount() {
        if(total >= ORDERS_LIMIT) {
            throw new OrderLimitException();
        }
        total++;
    }

    public DealService exchange() {
        countCumulative();
        DealService deals = new DealService();
        for(BigDecimal buyPrice: buyCumulative.navigableKeySet()) {
            BigDecimal sellPrice = sellCumulative.floorKey(buyPrice);
            if(sellPrice == null) continue;
            BigInteger sellCount = sellCumulative.get(sellPrice);
            BigInteger buyCount = buyCumulative.get(buyPrice);
            deals.rememberDeal(buyPrice, sellPrice, buyCount.min(sellCount));
        }
        return deals;
    }

    private void countCumulative() {
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
    }

    public boolean exchangePossible() {
        BigDecimal maxBuyPrice = buyOrders.navigableKeySet().last();
        BigDecimal minSellPrice = sellOrders.navigableKeySet().first();

        return maxBuyPrice.compareTo(minSellPrice) > 0;
    }
}
