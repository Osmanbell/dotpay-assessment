package com.example.dotpayassessment.boostrap;

import com.example.dotpayassessment.model.Customer;
import com.example.dotpayassessment.repository.CustomerRepo;
import com.example.dotpayassessment.utils.RandomAlphaNumericGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by @oladapoyuken 23/03/2022
 */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitDatabase implements CommandLineRunner {

    private final CustomerRepo customerRepo;

    private final RandomAlphaNumericGenerator generator;

    @Value("${summary.cron.expression}")
    private String summaryCron;

    @Value("${commission.cron.expression}")
    private String commissionCron;

    @Override
    public void run(String... args) throws Exception {
        showConfig();

        List<Customer> customers = customerRepo.findAll();

        if(customers.isEmpty())
            generateRandomCustomers();
    }

    private void generateRandomCustomers(){
        for(int i = 0; i < 10; i++){

            Customer customer = Customer.builder()
                    .accountNumber(generator.numeric(10))
                    .firstname(generator.alpha(5))
                    .lastname(generator.alpha(5))
                    .balance(Double.valueOf(generator.numeric(4)))
                    .build();

            customerRepo.save(customer);
        }
    }

    public void showConfig(){
        log.info("Summary cron job expression: {}", summaryCron);
        log.info("Commission cron job expression: {}", commissionCron);
    }
}
