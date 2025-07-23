package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;

public class TransactionListener {
    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "some-topic", groupId = "midas-core")
    public void listen(Transaction transaction) {
        boolean success = transactionService.processTransaction(transaction);
        System.out.println("Transaction " + (success ? "Succeeded" : "failed"));
    }
}
