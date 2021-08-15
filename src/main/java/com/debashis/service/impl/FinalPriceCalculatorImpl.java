package com.debashis.service.impl;

import com.debashis.model.TaxSlab;
import com.debashis.service.FinalPriceCalculator;
import com.debashis.service.TaxSlabProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
public class FinalPriceCalculatorImpl implements FinalPriceCalculator {

    private TaxSlabProvider taxSlabProvider;
    @Override
    public String calculateFinalPrice(String itemBasePrice,String discount,Set<TaxSlab> applicableTaxSlabs){
        BigDecimal totalTax = applicableTaxSlabs.stream().map(taxSlab -> taxSlab.calculateTax(itemBasePrice))
                .reduce(BigDecimal::add).orElse(new BigDecimal(0));
//        since there is only one obvious strategy to applying discount, so we simply hard code this algo.
        if(Objects.isNull(discount))
            discount = "0";
        BigDecimal discountTotal = new BigDecimal(itemBasePrice).multiply(new BigDecimal(discount))
                .divide(new BigDecimal(100));

        return new BigDecimal(itemBasePrice).add(totalTax).subtract(discountTotal)
                .setScale(2,RoundingMode.CEILING) // this should be picked from config
                .toString();
    }
    @Override
    public String calculateFinalPrice(String itemBasePrice,String discount){
        Set<TaxSlab> applicableTaxSlabs = taxSlabProvider.getApplicableTaxSlab(itemBasePrice);
        return calculateFinalPrice(itemBasePrice,discount,applicableTaxSlabs);
    }
}
