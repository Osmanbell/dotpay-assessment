package com.example.dotpayassessment.utils;

import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProcessSummaryTest.class)
class ProcessSummaryTest {

    @Mock
    TransactionRepo transactionRepo;

    @InjectMocks
    ProcessSummary processSummary;

    Transaction transaction;

    LocalDate date = LocalDate.now();

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .beneficiary("AAA")
                .amount(100L)
                .transactionFee(3L)
                .commission(1L)
                .status(Constants.SUCCESSFUL)
                .build();
    }

    @Test
    void dailySummary() {
        when(transactionRepo.findByDateCreated(date)).thenReturn(List.of(transaction));
        DailyTransactionSummary summary = processSummary.dailySummary(date);
        assertThat(summary.getTotalTransaction()).isEqualTo(100L);
        assertThat(summary.getTotalCommission()).isEqualTo(1L);
        assertThat(summary.getNumberOfSuccessfulTransactions()).isEqualTo(1);
    }
}