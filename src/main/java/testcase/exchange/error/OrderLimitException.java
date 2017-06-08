package testcase.exchange.error;

import testcase.exchange.Auction;

public class OrderLimitException extends ExchangeException {
    public OrderLimitException() {
        super(String.format("Order limit %d exceeded", Auction.ORDERS_LIMIT));
    }
}
