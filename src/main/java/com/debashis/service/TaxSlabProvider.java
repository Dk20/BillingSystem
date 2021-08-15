package com.debashis.service;

import com.debashis.model.TaxSlab;

import java.util.Set;

public interface TaxSlabProvider {
    Set<TaxSlab> getApplicableTaxSlab(String price);
    void addTaxSlab(String minPrice, String maxPrice, String tax, TaxSlab.TaxType type);
}
