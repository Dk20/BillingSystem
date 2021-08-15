package com.debashis.service.impl;

import com.debashis.model.TaxSlab;
import com.debashis.service.TaxSlabProvider;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class TaxSlabProviderImpl implements TaxSlabProvider {
    private AtomicInteger notProvidedMaxCount = new AtomicInteger(0);
    private AtomicInteger notProvidedMinCount = new AtomicInteger(0);
    private ConcurrentSkipListSet<TaxSlab> minSet = new ConcurrentSkipListSet<TaxSlab>(
            (t1,t2)->{
                BigDecimal b1 = new BigDecimal(t1.getMinPrice());
                BigDecimal b2 = new BigDecimal(t2.getMinPrice());
                return b1.compareTo(b2);
            });
    private ConcurrentSkipListSet<TaxSlab> maxSet = new ConcurrentSkipListSet<TaxSlab>(
            (t1,t2)->{
                BigDecimal b1 = new BigDecimal(t1.getMaxPrice());
                BigDecimal b2 = new BigDecimal(t2.getMaxPrice());
                return b1.compareTo(b2);
            });
    @Override
    public Set<TaxSlab> getApplicableTaxSlab(String price){
        Set<TaxSlab> intersection = new ConcurrentSkipListSet<>(minSet.headSet(TaxSlab.builder().minPrice(price).build()));
        intersection.retainAll(maxSet.tailSet(TaxSlab.builder().maxPrice(price).build()));
        return intersection;
    }
    @Override
    public void addTaxSlab(String minPrice, String maxPrice, String tax, TaxSlab.TaxType type){

        // just to handle null: set will ignore same key added twice, so MIN_VALUE cant be added twice
        if(Objects.isNull(minPrice)){
            minPrice = String.valueOf(Integer.MIN_VALUE+notProvidedMinCount.getAndIncrement());
        }
        if(Objects.isNull(maxPrice)){
            maxPrice = String.valueOf(Integer.MAX_VALUE-notProvidedMaxCount.getAndIncrement());
        }

        TaxSlab taxSlab = TaxSlab.builder()
                .maxPrice(maxPrice).minPrice(minPrice).tax(tax).type(type)
                .build();
        synchronized (this){
            minSet.add(taxSlab);maxSet.add(taxSlab);
        }
    }

}
