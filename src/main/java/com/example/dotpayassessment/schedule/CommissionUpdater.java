package com.example.dotpayassessment.schedule;

import com.example.dotpayassessment.model.DailyTransactionSummary;
import com.example.dotpayassessment.model.Transaction;
import com.example.dotpayassessment.repository.DailySummaryRepo;
import com.example.dotpayassessment.repository.TransactionRepo;
import com.example.dotpayassessment.utils.Constants;
import com.example.dotpayassessment.utils.ProcessCommission;
import com.example.dotpayassessment.utils.ProcessSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by @oladapoyuken 24/03/2022
 */

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CommissionUpdater {

    private final TransactionRepo transactionRepo;
    private final ProcessCommission processCommission;
    private final ProcessSummary processSummary;
    private final DailySummaryRepo summaryRepo;

    @Scheduled(cron = "${commission.cron.expression}")
    @Transactional
    public void CommissionHandler(){
        LocalDate currentDay = LocalDate.now();
        transactionRepo.findByDateCreated(currentDay).stream()
                        .filter(transaction -> transaction.getStatus().equals(Constants.SUCCESSFUL) &
                                transaction.getTransactionFee() > 0)
                        .forEach(processCommission::commission);

        log.info("Commission updater scheduler just completed!");
    }

    @Scheduled(cron = "${summary.cron.expression}")
    public void SummaryHandler(){
        LocalDate currentDay = LocalDate.now();
        DailyTransactionSummary summary = processSummary.dailySummary(currentDay);
        summaryRepo.save(summary);

       log.info("Daily transaction summary scheduler just completed");
    }
}
