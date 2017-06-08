package testcase.exchange;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.SortedMap;
import java.util.TreeMap;

@Getter
@EqualsAndHashCode
@ToString
public class DealService {
    private final SortedMap<BigDecimal, BigInteger> deals = new TreeMap<>();
    private BigDecimal optimalPrice = BigDecimal.ZERO;
    private BigInteger maxAmount = BigInteger.ZERO;

    public void rememberDeal(BigDecimal buyPrice, BigDecimal sellPrice, BigInteger amount) {
        BigDecimal price = buyPrice.add(sellPrice).divide(new BigDecimal(2), 2, BigDecimal.ROUND_UP);

        if(deals.containsKey(price)) {
            BigInteger existing = deals.get(price);
            deals.put(price, amount.max(existing));
        } else {
            deals.put(price, amount);
        }
        update(price, deals.get(price));
    }

    private final void update(BigDecimal price, BigInteger amount) {
        if(amount.compareTo(maxAmount) > 0) {
            maxAmount = amount;
            optimalPrice = price;
        }
    }
}
