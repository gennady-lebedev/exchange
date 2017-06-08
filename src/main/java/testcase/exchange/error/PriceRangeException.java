package testcase.exchange.error;

import testcase.exchange.Order;

import java.math.BigDecimal;

public class PriceRangeException extends ExchangeException {

    public PriceRangeException(BigDecimal price) {
        super(String.format(
                "Price %s out of range from %s to %s",
                price.toString(),
                Order.MIN_PRICE.toString(),
                Order.MAX_PRICE.toString()
        ));
    }
}
