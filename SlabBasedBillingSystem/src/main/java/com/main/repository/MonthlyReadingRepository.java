package com.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Customer;
import com.main.model.MonthlyReading;

@Repository
public interface MonthlyReadingRepository extends JpaRepository<MonthlyReading, Long> {
    List<MonthlyReading> findByCustomerAndDate(Customer customer, LocalDate date);
}