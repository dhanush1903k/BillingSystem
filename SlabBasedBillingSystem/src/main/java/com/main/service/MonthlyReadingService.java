package com.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.MonthlyReading;
import com.main.repository.MonthlyReadingRepository;

@Service
public class MonthlyReadingService {
    @Autowired
    private MonthlyReadingRepository monthlyReadingRepository;

    public MonthlyReading recordMonthlyReading(MonthlyReading reading) {
        return monthlyReadingRepository.save(reading);
    }
}