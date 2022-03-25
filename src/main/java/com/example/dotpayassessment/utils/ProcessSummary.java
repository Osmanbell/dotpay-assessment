package com.example.dotpayassessment.utils;

import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @oladapoyuken 24/03/2022
 */

@Component
@RequiredArgsConstructor
public class ProcessSummary {

    private final TransactionRepo transactionRepo;

    public DailyTransactionSummary dailySummary(LocalDate date){
        List<Transaction> transactions = transactionRepo.findByDateCreated(date);
        int size = transactions.size();
        int success = successTransaction(transactions);
        int failure = size - success;
        Map<String, Double> map = amountTransacted(transactions);

        DailyTransactionSummary summary = DailyTransactionSummary.builder()
                .numberOfTransactions(size)
                .numberOfSuccessfulTransactions(success)
                .numberOfFailedTransactions(failure)
                .totalCreditTransaction(map.get("credit"))
                .totalDebitTransaction(map.get("debit"))
                .totalTransaction(map.get("total"))
                .totalCommission(map.get("commission"))
                .totalTransactionFee(map.get("fee"))
                .transactionSummaryDate(date)
                .dateCreated(LocalDate.now())
                .build();

        return summary;
    }

    private int successTransaction(List<Transaction> transactions){
        return transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(Constants.SUCCESSFUL))
                .collect(Collectors.toList()).size();
    }

    private Map<String, Double> amountTransacted(List<Transaction> transactions){
        double commission = 0.0, credit = 0.0, debit = 0.0, fee = 0.0;

        for(Transaction transaction: transactions){
            if(transaction.getStatus().equals(Constants.SUCCESSFUL)){
                commission += transaction.getCommission();

                if(transaction.getTransactionType().equals(Constants.CREDIT)) {
                    credit += transaction.getAmount();
                }
                else {
                    debit += transaction.getAmount();
                }

                fee += transaction.getTransactionFee();
            }
        }
        Map<String, Double> map = new HashMap<>();
        map.put("commission", commission);
        map.put("credit", credit);
        map.put("debit", debit);
        map.put("fee", fee);
        map.put("total", credit + debit);

        return map;
    }


}
