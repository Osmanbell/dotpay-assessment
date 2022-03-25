package com.example.dotpayassessment.api;

import com.example.dotpayassessment.dto.Transact;
import com.example.dotpayassessment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Created by @oladapoyuken 23/03/2022
 */

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getTransactions(@RequestParam(required = false) String status,
                                             @RequestParam(required = false) String accountNumber,
                                             @RequestParam(required = false) String from,
                                             @RequestParam(required = false) String to
    ){
        return new ResponseEntity<>(transactionService.getTransactions(status, accountNumber, to, from), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> makeTransaction(@RequestBody Transact transact){
        return new ResponseEntity<>(transactionService.makeTransaction(transact), HttpStatus.CREATED);

    }

    @GetMapping("/summary")
    public ResponseEntity<?> getTransactionSummary(@RequestParam String date){
        return new ResponseEntity<>(transactionService.getSummary(date), HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getAccounts(){
        return new ResponseEntity<>(transactionService.getAccounts(), HttpStatus.OK);
    }

}
