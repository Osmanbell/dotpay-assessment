package com.example.dotpayassessment.repository;
import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by @oladapoyuken 23/03/2022
 */
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateCreated(LocalDate date);

    List<Transaction> findByStatus(String status);

    List<Transaction> findByCustomer(Customer customer);

    List<Transaction> findByDateCreatedBetween(LocalDate from, LocalDate to);

    List<Transaction> findByCustomerAndStatus(Customer customer, String status);

    List<Transaction> findByCustomerAndDateCreatedBetween(Customer customer, LocalDate from, LocalDate to);

    List<Transaction> findByStatusAndDateCreatedBetween(String status, LocalDate from, LocalDate to);

    List<Transaction> findByCustomerAndStatusAndDateCreatedBetween(Customer customer, String status, LocalDate from, LocalDate to);


}
