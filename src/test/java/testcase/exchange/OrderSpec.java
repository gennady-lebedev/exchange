package testcase.exchange;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import testcase.exchange.error.OrderAmountRangeException;
import testcase.exchange.error.PriceFormatException;
import testcase.exchange.error.PriceRangeException;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class OrderSpec {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void allowedPriceFormatsAndRange() {
        assertEquals("Price '1' is allowed", new BigDecimal("1.00"), new Order("1", "1").getPrice());
        assertEquals("Price '1.0' is allowed", new BigDecimal("1.00"), new Order("1.0", "1").getPrice());
        assertEquals("Price '1.00' is allowed", new BigDecimal("1.00"), new Order("1.00", "1").getPrice());
        assertEquals("Price '1.1' is allowed", new BigDecimal("1.10"), new Order("1.1", "1").getPrice());
        assertEquals("Price '1.01' is allowed", new BigDecimal("1.01"), new Order("1.01", "1").getPrice());
        assertEquals("Price '100' is allowed", new BigDecimal("100.00"), new Order("100", "1").getPrice());
        assertEquals("Price '100.00' is allowed", new BigDecimal("100.00"), new Order("100.00", "1").getPrice());
    }

    @Test
    public void priceWithAllowedPrecisionShouldBeEqual() {
        assertEquals("Prices with same amount but different precision should be equal", new Order("1", "1"), new Order("1.0", "1"));
        assertEquals("Prices with same amount but different precision should be equal", new Order("1.0", "1"), new Order("1.00", "1"));
        assertEquals("Prices with same amount but different precision should be equal", new Order("1", "1"), new Order("1.00", "1"));
    }

    @Test
    public void priceShouldBeLessThan100() {
        thrown.expect(PriceRangeException.class);
        new Order("100.01", "1");
    }

    @Test
    public void priceShouldBeGreaterThan1() {
        thrown.expect(PriceRangeException.class);
        new Order("0.99", "1");
    }

    @Test
    public void pricePrecisionShouldBeNotMoreThan2Digits() {
        thrown.expect(PriceFormatException.class);
        new Order("1.001", "1");
    }

    @Test
    public void amountShouldBeLessThan1000() {
        thrown.expect(OrderAmountRangeException.class);
        new Order("1", "1001");
    }

    @Test
    public void amountShouldBeMoreThan1() {
        thrown.expect(OrderAmountRangeException.class);
        new Order("1", "0");
    }
}
