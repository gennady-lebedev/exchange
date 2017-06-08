package testcase.exchange.error;

public class PriceFormatException extends ExchangeException {
    public PriceFormatException(String price) {
        super(String.format("Malformed price format in '%s'", price));
    }
}
