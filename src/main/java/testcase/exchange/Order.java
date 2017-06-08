package testcase.exchange;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import testcase.exchange.error.OrderAmountRangeException;
import testcase.exchange.error.OrderFormatException;
import testcase.exchange.error.PriceFormatException;
import testcase.exchange.error.PriceRangeException;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@EqualsAndHashCode
public class Order {
    private BigDecimal price;
    private BigInteger amount;

    public static final BigDecimal MAX_PRICE = new BigDecimal("100");
    public static final BigDecimal MIN_PRICE = new BigDecimal("1");
    public static final BigInteger MAX_AMOUNT = new BigInteger("1000");
    public static final BigInteger MIN_AMOUNT = new BigInteger("1");

    public Order(String price, String amount) {
        try {
            this.price = new BigDecimal(price);
        } catch (NumberFormatException e) {
            throw new PriceFormatException();
        }
        try {
            this.amount = new BigInteger(amount);
        } catch (NumberFormatException e) {
            throw new OrderFormatException();
        }

        if(this.price.scale() > 2) {
            throw new PriceFormatException();
        } else {
            this.price = this.price.setScale(2);
        }

        if(this.price.compareTo(MAX_PRICE) > 0 || this.price.compareTo(MIN_PRICE) < 0) {
            throw new PriceRangeException();
        }

        if(this.amount.compareTo(MAX_AMOUNT) > 0 || this.amount.compareTo(MIN_AMOUNT) < 0) {
            throw new OrderAmountRangeException();
        }
    }
}
