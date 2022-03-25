package com.example.dotpayassessment.service;

import com.example.dotpayassessment.dto.Transact;
import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.CustomerRepo;
import com.example.dotpayassessment.repository.DailySummaryRepo;
import com.example.dotpayassessment.repository.TransactionRepo;
import com.example.dotpayassessment.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TransactionServiceImplTest.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepo transactionRepo;

    @Mock
    CustomerRepo customerRepo;

    @Mock
    DailySummaryRepo dailySummaryRepo;

    @InjectMocks
    TransactionServiceImpl transactionService;

    Transaction transaction;

    Transact transact;

    Customer customer;

    Customer customer1;

    DailyTransactionSummary summary;

    LocalDate date = LocalDate.now();

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .beneficiary("AAA")
                .amount(100.00)
                .status(Constants.SUCCESSFUL)
                .build();

        transact = new Transact(
                "1234", "5678", 50L, "TEST"
        );

        customer = Customer.builder()
                .lastname("AAA")
                .firstname("BBB")
                .accountNumber("1234")
                .balance(100L)
                .build();

        customer1 = Customer.builder()
                .lastname("AAA")
                .firstname("BBB")
                .accountNumber("5678")
                .balance(100L)
                .build();

        summary = DailyTransactionSummary.builder()
                .numberOfTransactions(2)
                .numberOfSuccessfulTransactions(2)
                .totalTransaction(100L)
                .transactionSummaryDate(date)
                .build();
    }


    @Test
    void getTransactionsAll() {
        List<Transaction> list = List.of(transaction);
        when(transactionRepo.findAll()).thenReturn(list);
        List<Transaction> transactions = transactionService.getTransactions(null, null, null, null);
        assertThat(transactions.size()).isEqualTo(1);

    }

    @Test
    void makeTransaction() {
        when(customerRepo.findByAccountNumber("1234"))
                .thenReturn(Optional.ofNullable(customer));

        when(customerRepo.findByAccountNumber("5678"))
                .thenReturn(Optional.ofNullable(customer));

        Transaction transaction = transactionService.makeTransaction(transact);
        assertThat(transaction.getStatus().equals(Constants.SUCCESSFUL));
        verify(transactionRepo, times(1)).save(transaction);
    }

    @Test
    void getSummary() {
        when(dailySummaryRepo.findByTransactionSummaryDate(date)).thenReturn(Optional.ofNullable(summary));
        DailyTransactionSummary summary1 = transactionService.getSummary(String.valueOf(date));

        assertThat(summary1.equals(summary));

    }

    @Test
    void getAccounts() {
    }
}