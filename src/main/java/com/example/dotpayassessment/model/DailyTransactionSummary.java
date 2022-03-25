package com.example.dotpayassessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by @oladapoyuken 23/03/2022
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyTransactionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int numberOfTransactions;
    private int numberOfSuccessfulTransactions;
    private int numberOfFailedTransactions;
    private double totalCreditTransaction;
    private double totalDebitTransaction;
    private double totalTransaction;
    private double totalCommission;
    private double totalTransactionFee;
    private LocalDate transactionSummaryDate;
    private LocalDate dateCreated;

}
