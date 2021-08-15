package com.debashis.service;

import com.debashis.model.TaxSlab;

import java.util.Set;

public interface FinalPriceCalculator {
    String calculateFinalPrice(String itemBasePrice,String discount);
    String calculateFinalPrice(String itemBasePrice, String discount, Set<TaxSlab> applicableTaxSlabs);
}
