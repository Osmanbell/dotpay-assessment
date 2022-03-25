package com.example.dotpayassessment.service;

import com.example.dotpayassessment.dto.Transact;
import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by @oladapoyuken 23/03/2022
 */
public interface TransactionService {

    List<Transaction> getTransactions(String status, String userid, String to, String from);

    Transaction makeTransaction(Transact transact);

    DailyTransactionSummary getSummary(String date);

    List<Customer> getAccounts();

    void saveTransaction(Transaction transaction);
}
