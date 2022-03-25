package com.example.dotpayassessment.utils;

import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.CustomerRepo;
import com.example.dotpayassessment.repository.TransactionRepo;
import com.example.dotpayassessment.service.TransactionService;
import com.example.dotpayassessment.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProcessCommissionTest.class)
class ProcessCommissionTest {

    Transaction transaction;

    Customer customer;

    @Mock
    TransactionRepo transactionRepo;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    ProcessCommission processCommission;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .balance(100L)
                .build();

        transaction = Transaction.builder()
                .customer(customer)
                .transactionFee(20L)
                .build();
    }

    @Test
    void commission() {
        Transaction transaction1 = processCommission.commission(transaction);
        verify(transactionService, times(1)).saveTransaction(Mockito.any());
        assertThat(transaction1.getCommission()).isEqualTo(20 * 0.2);


    }
}