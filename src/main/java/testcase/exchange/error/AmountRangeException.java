package testcase.exchange.error;

import testcase.exchange.Order;

import java.math.BigInteger;

public class AmountRangeException extends ExchangeException {
    public AmountRangeException(BigInteger amount) {
        super(String.format(
                "Amount %s out of range from %s to %s",
                amount.toString(),
                Order.MIN_AMOUNT.toString(),
                Order.MAX_AMOUNT.toString()
                ));
    }
}
