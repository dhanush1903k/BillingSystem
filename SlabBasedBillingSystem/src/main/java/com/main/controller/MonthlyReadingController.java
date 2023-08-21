package com.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.MonthlyReading;
import com.main.service.MonthlyReadingService;

@RestController
@RequestMapping("/api/monthlyreading")
public class MonthlyReadingController {
    @Autowired
    private MonthlyReadingService monthlyReadingService;

    @PostMapping
    public ResponseEntity<MonthlyReading> recordMonthlyReading(@RequestBody MonthlyReading reading) {
        MonthlyReading savedReading = monthlyReadingService.recordMonthlyReading(reading);
        return new ResponseEntity<>(savedReading, HttpStatus.CREATED);
    }
}