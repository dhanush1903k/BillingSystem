package com.main.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.Bill;
import com.main.model.Customer;
import com.main.repository.CustomerRepository;
import com.main.service.BillService;

@RestController
@RequestMapping("/api/bill")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Bill> generateBill(
            @RequestParam Long customerId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
    
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (!optionalCustomer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = optionalCustomer.get();

        Bill bill = billService.generateBill(customer, date);

        if (bill == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bill, HttpStatus.OK);
    }
}
