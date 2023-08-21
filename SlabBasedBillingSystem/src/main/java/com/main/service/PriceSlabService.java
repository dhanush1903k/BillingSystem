package com.main.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.PriceSlab;
import com.main.repository.PriceSlabRepository;

@Service
public class PriceSlabService {
    @Autowired
    private PriceSlabRepository priceSlabRepository;

    public PriceSlab definePriceSlab(PriceSlab priceSlab) {
        return priceSlabRepository.save(priceSlab);
    }

    public List<PriceSlab> getPriceSlabsForPeriod(LocalDate startDate, LocalDate endDate) {
        return priceSlabRepository.findByStartDateBeforeAndEndDateAfter(startDate, endDate);
    }

    public PriceSlab getApplicablePriceSlab(LocalDate date) {
        List<PriceSlab> applicableSlabs = getPriceSlabsForPeriod(date, date);

        
        PriceSlab applicableSlab = null;
        LocalDate latestStartDate = LocalDate.MIN;

        for (PriceSlab slab : applicableSlabs) {
            if (slab.getStartDate().isAfter(latestStartDate)) {
                latestStartDate = slab.getStartDate();
                applicableSlab = slab;
            }
        }

        return applicableSlab;
    }
}
