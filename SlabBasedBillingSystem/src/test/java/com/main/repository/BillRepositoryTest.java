package com.main.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.main.model.Bill;
import com.main.repository.BillRepository;
import com.main.repository.CustomerRepository;
import com.main.model.Customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.main.model.Bill;
import com.main.repository.BillRepository;
import com.main.model.Customer;

@ExtendWith(SpringExtension.class) 
@DataJpaTest
public class BillRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BillRepository billRepository;

    @Test
    public void testFindById() {
       
        Customer customer = new Customer();
        customer.setName("John Doe");
        testEntityManager.persist(customer);

        
        Bill bill = new Bill();
        bill.setCustomer(customer);
        bill.setTotalAmount(100.0);
        testEntityManager.persist(bill);

     
        Bill foundBill = billRepository.findById(bill.getId()).orElse(null);

        
        assertThat(foundBill).isNotNull();
        assertThat(foundBill.getTotalAmount()).isEqualTo(100.0);
        assertThat(foundBill.getCustomer()).isEqualTo(customer);
    }

    
}


