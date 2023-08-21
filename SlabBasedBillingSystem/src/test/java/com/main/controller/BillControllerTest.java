package com.main.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.main.controller.BillController;
import com.main.exceptions.CustomerNotFoundException;
import com.main.exceptions.RestExceptionHandler;
import com.main.model.Bill;
import com.main.service.BillService;
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BillControllerTest {

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        
        mockMvc = MockMvcBuilders.standaloneSetup(billController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void testGenerateBillWithValidParameters() throws Exception {
     
   
        Bill bill = new Bill();
        bill.setTotalAmount(50.0); 

       
        when(billService.generateBill(any(), any())).thenReturn(bill);


        mockMvc.perform(get("/api/bill/generate")
                .param("customerId", "1")
                .param("date", "2023-08-15"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.totalAmount", is(50.0))); 
    }

    @Test
    public void testGenerateBillWithInvalidCustomerId() throws Exception {
       
        when(billService.generateBill(any(), any())).thenThrow(new CustomerNotFoundException("Customer not found"));

        
        mockMvc.perform(get("/api/bill/generate")
                .param("customerId", "999") 
                .param("date", "2023-08-15"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.message", is("Customer not found"))); 
    }

    @Test
    public void testGenerateBillWithNoApplicableSlab() throws Exception {
       
        Bill bill = new Bill();
        bill.setTotalAmount(0.0);
        when(billService.generateBill(any(), any())).thenReturn(bill);

        
        mockMvc.perform(get("/api/bill/generate")
                .param("customerId", "1")
                .param("date", "2023-08-15"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalAmount", is(0.0)));     
}
}