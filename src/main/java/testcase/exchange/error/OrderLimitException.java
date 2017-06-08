package testcase.exchange.error;

import testcase.exchange.DiscreteAuction;

public class OrderLimitException extends ExchangeException {
    public OrderLimitException() {
        super(String.format("Order limit %d exceeded", DiscreteAuction.ORDERS_LIMIT));
    }
}
