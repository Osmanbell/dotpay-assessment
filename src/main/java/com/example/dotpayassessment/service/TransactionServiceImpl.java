package com.example.dotpayassessment.service;

import com.example.dotpayassessment.dto.Transact;
import com.example.dotpayassessment.exception.ApiNotFoundException;
import com.example.dotpayassessment.exception.ApiRequestException;
import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.CustomerRepo;
import com.example.dotpayassessment.repository.DailySummaryRepo;
import com.example.dotpayassessment.repository.TransactionRepo;
import com.example.dotpayassessment.utils.Constants;
import com.example.dotpayassessment.utils.ProcessSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by @oladapoyuken 23/03/2022
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final CustomerRepo customerRepo;
    private final DailySummaryRepo summaryRepo;
    private final ProcessSummary processSummary;

    @Override
    public List<Transaction> getTransactions(String status, String accountNumber, String to, String from) {
        if(status != null && accountNumber == null && (to == null || from == null)) {
            return transactionRepo.findByStatus(status);
        }
        else if(status == null && accountNumber != null && (to == null || from == null)){
            Customer customer = getCustomer(accountNumber);
            return transactionRepo.findByCustomer(customer);
        }
        else if(status == null && accountNumber == null && (to != null && from != null)){

            return transactionRepo.findByDateCreatedBetween(LocalDate.parse(from), LocalDate.parse(to));
        }
        else if(status != null && accountNumber != null && (to == null || from == null)){
            Customer customer = getCustomer(accountNumber);
            return transactionRepo.findByCustomerAndStatus(customer, status);
        }
        else if(status != null && accountNumber == null && (to != null && from != null)){
            return transactionRepo.findByStatusAndDateCreatedBetween(status, LocalDate.parse(from), LocalDate.parse(to));
        }
        else if(status == null && accountNumber != null && (to != null && from != null)){
            Customer customer = getCustomer(accountNumber);
            return transactionRepo.findByCustomerAndDateCreatedBetween(customer, LocalDate.parse(from), LocalDate.parse(to));
        }
        else if(status != null && accountNumber != null && (to != null && from != null)){
            Customer customer = getCustomer(accountNumber);
            return transactionRepo.findByCustomerAndStatusAndDateCreatedBetween(customer, status, LocalDate.parse(from), LocalDate.parse(to));
        }

        return transactionRepo.findAll();

    }

    @Override
    @Transactional
    public Transaction makeTransaction(Transact transact) {
        Customer sender = getCustomer(transact.getSenderAccountNumber());
        Customer beneficiary = getCustomer(transact.getBeneficiaryAccountNumber());
        Transaction transaction = Transaction.builder()
                .sender(transact.getSenderAccountNumber())
                .customer(sender)
                .amount(transact.getAmount())
                .beneficiary(beneficiary.getAccountNumber())
                .description(transact.getDescription())
                .dateCreated(LocalDate.now())
                .build();
        double transactionFee = new BigDecimal(transact.getAmount() * 0.005).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double balance = sender.getBalance() - transact.getAmount() - transactionFee;

        if(balance <= 0) {
            transaction.setCommissionWorthy(false);
            transaction.setAvailableBalance(sender.getBalance());
            transaction.setStatus(Constants.INSUFFICIENT_FUND);
            transaction.setTransactionType(Constants.DEBIT);

            transactionRepo.save(transaction);
            return transaction;
        }
        transaction.setBilledAmount(transactionFee);
        transaction.setTransactionFee(transactionFee);
        transaction.setStatus(Constants.SUCCESSFUL);
        transaction.setAvailableBalance(balance);
        transaction.setTransactionType(Constants.DEBIT);

        beneficiary.setBalance(beneficiary.getBalance() + transact.getAmount());
        sender.setBalance(balance);
        transactionRepo.save(transaction);

        beneficiaryTransaction(transaction, beneficiary);

        return transaction;
    }

    @Override
    public DailyTransactionSummary getSummary(String date) {

        if(date == null)
            throw new ApiRequestException("Date not valid");

        Optional<DailyTransactionSummary> optional =  summaryRepo.findByTransactionSummaryDate(LocalDate.parse(date));

        return optional.isPresent() ? optional.get() : processSummary.dailySummary(LocalDate.parse(date));

    }

    @Override
    public List<Customer> getAccounts() {
        return customerRepo.findAll();
    }

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepo.save(transaction);
    }

    private Customer getCustomer(String accountNumber){
        Optional<Customer> optional = customerRepo.findByAccountNumber(accountNumber);
        if(optional.isEmpty())
            throw new ApiNotFoundException("Account number: " + accountNumber + " not found!");

        return optional.get();
    }

    private void beneficiaryTransaction(Transaction transaction, Customer customer){
        Transaction beneTransaction = Transaction.builder()
                .sender(transaction.getSender())
                .customer(customer)
                .commissionWorthy(false)
                .amount(transaction.getAmount())
                .beneficiary(customer.getAccountNumber())
                .transactionType(Constants.CREDIT)
                .status(Constants.SUCCESSFUL)
                .availableBalance(customer.getBalance())
                .description(transaction.getDescription())
                .dateCreated(LocalDate.now())
                .build();

        transactionRepo.save(beneTransaction);
    }
}
