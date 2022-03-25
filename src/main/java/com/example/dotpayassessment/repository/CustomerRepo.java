package com.example.dotpayassessment.repository;

import com.example.dotpayassessment.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by @oladapoyuken 23/03/2022
 */
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountNumber(String accountNumber);
}
