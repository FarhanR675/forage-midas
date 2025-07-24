package com.jpmc.midascore.foundation;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository;
    private final RestTemplate restTemplate;

    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    public boolean processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null || sender.getBalance() < transaction.getAmount()) {
            return false;
        }
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        Incentive incentive = fetchIncentive(transaction);

        if(incentive != null && incentive.getAmount() > 0) {
            recipient.setBalance(recipient.getBalance() + incentive.getAmount());
        }

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                transaction.getAmount()
        );
        transactionRepository.save(record);
        return true;
    }

    private Incentive fetchIncentive(Transaction transaction) {
        try {
            ResponseEntity<Incentive> response = restTemplate.postForEntity(
                    "http://localhost:8080/incentive",
                    transaction,
                    Incentive.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
