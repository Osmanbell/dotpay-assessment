package com.example.dotpayassessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by @oladapoyuken 23/03/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transact {

    private String SenderAccountNumber;
    private String BeneficiaryAccountNumber;
    private double amount;
    private String description;
}
