package com.main.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.Bill;
import com.main.model.Customer;
import com.main.model.MonthlyReading;
import com.main.model.PriceSlab;
import com.main.repository.BillRepository;
import com.main.repository.MonthlyReadingRepository;

@Service
public class BillService {
    @Autowired
    private MonthlyReadingRepository readingRepository;
    @Autowired
    private PriceSlabService priceSlabService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    public BillService(PriceSlabService priceSlabService, MonthlyReadingRepository readingRepository, BillRepository billRepository) {
        this.priceSlabService = priceSlabService;
        this.readingRepository = readingRepository;
        this.billRepository = billRepository;
    }

    public Bill generateBill(Customer customer, LocalDate date) {
       
        PriceSlab applicableSlab = priceSlabService.getApplicablePriceSlab(date);

        if (applicableSlab == null) {
            // case where no applicable price slab is found
            Bill noApplicableSlabBill = new Bill();
            noApplicableSlabBill.setCustomer(customer);
            noApplicableSlabBill.setDate(date);
            noApplicableSlabBill.setTotalAmount(0.0); 
            return noApplicableSlabBill;
        }

        // Fetching the latest two monthly readings for the customer
        List<MonthlyReading> readings = readingRepository.findByCustomerAndDate(customer, date);
        if (readings.size() < 2) {
            // case where there are not enough readings for calculation
            Bill insufficientReadingsBill = new Bill();
            insufficientReadingsBill.setCustomer(customer);
            insufficientReadingsBill.setDate(date);
            insufficientReadingsBill.setTotalAmount(0.0); // Set a special value or status
            return insufficientReadingsBill;
        }

        MonthlyReading currentReading = readings.get(0);
        MonthlyReading previousReading = readings.get(1);

        // Calculation of unit of consumption and bill amount
        int unitOfConsumption = currentReading.getCurrentReading() - previousReading.getCurrentReading();
        double slabRate = applicableSlab.getSlabRate();
        double billAmount = unitOfConsumption * slabRate;

        // save the bill
        Bill bill = new Bill();
        bill.setCustomer(customer);
        bill.setDate(date);
        bill.setTotalAmount(billAmount);

        return billRepository.save(bill);
    }
}
