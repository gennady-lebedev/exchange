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
public class Auction {
    private static final Logger log = LoggerFactory.getLogger(Auction.class);

    public static final int ORDERS_LIMIT = 1000000;

    private final NavigableMap<BigDecimal, BigInteger> buyOrders;
    private final NavigableMap<BigDecimal, BigInteger> sellOrders;

    private int total = 0;


    public Auction() {
        this.buyOrders = new TreeMap<>();
        this.sellOrders = new TreeMap<>();
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
            BigInteger current = orders.get(price);
            log.trace("Stack amount {} and {} by price {}", amount, current, price);
            amount = amount.add(current);
        }
        orders.put(price, amount);
    }

    private void checkAndCount() {
        if(total >= ORDERS_LIMIT) {
            throw new OrderLimitException();
        }
        total++;
    }

    public PossibleDeals exchange() {
        Cumulative cumulative = new Cumulative(buyOrders, sellOrders);
        PossibleDeals deals = new PossibleDeals(cumulative);
        log.trace("Possible deals: {}", deals);
        deals.findOptimalPrice();
        log.debug("Optimal prise is {} with max amount {}", deals.getOptimalPrice(), deals.getMaxAmount());
        return deals;
    }

    public boolean exchangePossible() {
        BigDecimal maxBuyPrice = buyOrders.navigableKeySet().last();
        BigDecimal minSellPrice = sellOrders.navigableKeySet().first();

        return maxBuyPrice.compareTo(minSellPrice) > 0;
    }
}
