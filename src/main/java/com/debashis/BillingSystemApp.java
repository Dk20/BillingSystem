package com.debashis;


import com.debashis.model.TaxSlab;
import com.debashis.service.FinalPriceCalculator;
import com.debashis.service.TaxSlabProvider;
import com.debashis.service.impl.FinalPriceCalculatorImpl;
import com.debashis.service.impl.TaxSlabProviderImpl;

public class BillingSystemApp {

    public static void main(String[] args) {
        TaxSlabProvider taxSlabProvider = new TaxSlabProviderImpl();
        taxSlabProvider.addTaxSlab("1000","7500","12", TaxSlab.TaxType.PERCENTAGE);
        taxSlabProvider.addTaxSlab("7500",null,"18", TaxSlab.TaxType.PERCENTAGE);
        taxSlabProvider.addTaxSlab("0",null,"200", TaxSlab.TaxType.FIXED);
        FinalPriceCalculator finalPriceCalculator = new FinalPriceCalculatorImpl(taxSlabProvider);
        System.out.println(finalPriceCalculator.calculateFinalPrice("120000","5"));
        System.out.println(finalPriceCalculator.calculateFinalPrice("2000",null));

    }


}
