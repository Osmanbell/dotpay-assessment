package com.example.dotpayassessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by @oladapoyuken 23/03/2022
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sender;
    private String beneficiary;
    private double amount;
    private double availableBalance;
    private String transactionType;
    private double transactionFee;
    private double billedAmount;
    private String description;
    private String status;
    private boolean commissionWorthy;
    private double commission;
    private LocalDate dateCreated;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

}
