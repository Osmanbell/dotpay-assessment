package com.example.dotpayassessment.utils;

import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.CustomerRepo;
import com.example.dotpayassessment.repository.TransactionRepo;
import com.example.dotpayassessment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by @oladapoyuken 24/03/2022
 */

@Component
@RequiredArgsConstructor
public class ProcessCommission {

    private final TransactionRepo transactionRepo;
    private final CustomerRepo customerRepo;
    private final TransactionService transactionService;

    public Transaction commission(Transaction transaction){
        Customer customer = transaction.getCustomer();
        double fee = getCommission(transaction.getTransactionFee());
        double balance = customer.getBalance() - fee;
        if(balance > 0){
            customer.setBalance(balance);
            Transaction commTransaction = Transaction.builder()
                    .transactionType(Constants.DEBIT)
                    .customer(customer)
                    .sender("NA")
                    .beneficiary("NA")
                    .amount(fee)
                    .availableBalance(balance)
                    .billedAmount(fee)
                    .description("Commission on transaction")
                    .status(Constants.SUCCESSFUL)
                    .dateCreated(LocalDate.now())
                    .build();

            transactionService.saveTransaction(commTransaction);

            transaction.setCommissionWorthy(true);
            transaction.setCommission(fee);
            transaction.setBilledAmount(fee + transaction.getBilledAmount());
            transaction.setCommissionWorthy(true);
        }

        return transaction;
    }

    private double getCommission(double transactionFee){
        return new BigDecimal(transactionFee * 0.2).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
