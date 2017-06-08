package testcase.exchange;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
@EqualsAndHashCode
@ToString
public class PossibleDeals {
    private final NavigableMap<BigDecimal, BigInteger> deals = new TreeMap<>();
    private BigDecimal optimalPrice = BigDecimal.ZERO;
    private BigInteger maxAmount = BigInteger.ZERO;

    public PossibleDeals(Cumulative cumulative) {
        NavigableMap<BigDecimal, BigInteger> buyCumulative = cumulative.getBuyCumulative();
        NavigableMap<BigDecimal, BigInteger> sellCumulative = cumulative.getSellCumulative();

        for(BigDecimal buyPrice: buyCumulative.tailMap(sellCumulative.firstKey(), true).keySet()) {
            BigDecimal sellPrice = sellCumulative.floorKey(buyPrice);
            if(sellPrice == null) continue;
            BigInteger sellCount = sellCumulative.get(sellPrice);
            BigInteger buyCount = buyCumulative.get(buyPrice);
            rememberDeal(buyPrice, sellPrice, buyCount.min(sellCount));
        }
        findOptimalPrice();
    }

    private void rememberDeal(BigDecimal buyPrice, BigDecimal sellPrice, BigInteger amount) {
        mergeDeals(buyPrice, amount);
        if(!buyPrice.equals(sellPrice)) {
            mergeDeals(sellPrice, amount);
        }
    }

    private void mergeDeals(BigDecimal price, BigInteger amount) {
        if(deals.containsKey(price)) {
            BigInteger existing = deals.get(price);
            deals.put(price, amount.max(existing));
        } else {
            deals.put(price, amount);
        }
    }

    private void findOptimalPrice() {
        BigDecimal minPrice = BigDecimal.ZERO;
        BigDecimal maxPrice = BigDecimal.ZERO;
        for(BigDecimal price: deals.navigableKeySet()) {
            BigInteger amount = deals.get(price);
            if(amount.compareTo(maxAmount) > 0) {
                maxAmount = amount;
                minPrice = price;
                maxPrice = price;
            } else if(amount.compareTo(maxAmount) == 0) {
                maxPrice = price;
            }
        }
        optimalPrice = minPrice.add(maxPrice).divide(new BigDecimal(2), 2, BigDecimal.ROUND_UP);
    }
}
