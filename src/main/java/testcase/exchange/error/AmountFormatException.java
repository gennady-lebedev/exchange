package testcase.exchange.error;

public class AmountFormatException extends ExchangeException {
    public AmountFormatException(String amount) {
        super(String.format("Malformed amount format in '%s'", amount));
    }
}
