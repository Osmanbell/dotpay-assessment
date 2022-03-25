package com.example.dotpayassessment.repository;

import com.example.dotpayassessment.model.DailyTransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by @oladapoyuken 23/03/2022
 */
public interface DailySummaryRepo extends JpaRepository<DailyTransactionSummary, Long> {
    Optional<DailyTransactionSummary> findByTransactionSummaryDate(LocalDate date);
}
