package com.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
