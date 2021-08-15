package com.debashis.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

@Builder
@Getter
@ToString
public class TaxSlab {
    public enum TaxType{PERCENTAGE,FIXED}
    private String minPrice;
    private String maxPrice;
    private String tax;
    private TaxType type;
    private Function<String,BigDecimal> calcStrategy;
    // calculates tax for this slab
    public BigDecimal calculateTax(String basePrice){
        switch (type){
            case PERCENTAGE:
                calcStrategy = basePricee -> {
                    BigDecimal base = new BigDecimal(basePricee);
                    return base.multiply(new BigDecimal(this.tax)).divide(new BigDecimal(100));
                };
                break;
            case FIXED:
                calcStrategy = basePricee -> new BigDecimal(this.tax);
                break;
//            case CUSTOM:  will use existing calculation strategy supplied at the time of creation
            default:
                break;
        }
        return calcStrategy.apply(basePrice);
    }
}
