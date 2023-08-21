package com.main.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.main.model.Bill;
import com.main.model.Customer;
import com.main.model.MonthlyReading;
import com.main.model.PriceSlab;
import com.main.repository.BillRepository;
import com.main.repository.MonthlyReadingRepository;


@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest {

    @Mock
    private MonthlyReadingRepository readingRepository;

    @Mock
    private PriceSlabService priceSlabService;

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    @Before
    public void setUp() {
        
        billService = new BillService(priceSlabService, readingRepository, billRepository);
    }

    @Test
    public void testGenerateBillWithApplicableSlab() {

        PriceSlab applicableSlab = new PriceSlab();
        applicableSlab.setStartDate(LocalDate.of(2023, 8, 1));
        applicableSlab.setEndDate(LocalDate.of(2023, 8, 31));
        applicableSlab.setSlabRate(0.15);

        MonthlyReading currentReading = new MonthlyReading();
        currentReading.setCurrentReading(500);
        MonthlyReading previousReading = new MonthlyReading();
        previousReading.setCurrentReading(450);

        Customer customer = new Customer();

        when(priceSlabService.getApplicablePriceSlab(any())).thenReturn(applicableSlab);
        when(readingRepository.findByCustomerAndDate(any(), any()))
                .thenReturn(Arrays.asList(currentReading, previousReading));
        when(billRepository.save(any())).thenReturn(new Bill());

        Bill bill = billService.generateBill(customer, LocalDate.of(2023, 8, 15));

        assertNotNull(bill);
        assertEquals(7.5, bill.getTotalAmount(), 0.01);
    }

    @Test
    public void testGenerateBillWithNoApplicableSlab() {

        when(priceSlabService.getApplicablePriceSlab(any())).thenReturn(null);
        Customer customer = new Customer();

        Bill bill = billService.generateBill(customer, LocalDate.of(2023, 8, 15));

        assertNotNull(bill);
        assertEquals(0.0, bill.getTotalAmount(), 0.01);
    }

    @Test
    public void testGenerateBillWithInsufficientReadings() {

        PriceSlab applicableSlab = new PriceSlab();
        applicableSlab.setStartDate(LocalDate.of(2023, 8, 1));
        applicableSlab.setEndDate(LocalDate.of(2023, 8, 31));
        applicableSlab.setSlabRate(0.15);

        MonthlyReading currentReading = new MonthlyReading();
        currentReading.setCurrentReading(500);

        Customer customer = new Customer();

        when(priceSlabService.getApplicablePriceSlab(any())).thenReturn(applicableSlab);
        when(readingRepository.findByCustomerAndDate(any(), any()))
                .thenReturn(Collections.singletonList(currentReading));

        Bill bill = billService.generateBill(customer, LocalDate.of(2023, 8, 15));

        assertNotNull(bill);
        assertEquals(0.0, bill.getTotalAmount(), 0.01);
    }
}

