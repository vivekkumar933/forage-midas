package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncentiveService incentiveService;

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-group"
    )
    public void listen(Transaction transaction) {

        // Fetch sender and receiver
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord receiver = userRepository.findById(transaction.getRecipientId());

        // Validate users
        if (sender == null || receiver == null) return;

        float amount = (float) transaction.getAmount();

        // Validate sufficient balance
        if (sender.getBalance() < amount) return;

        // Get incentive from external API
        float incentive = incentiveService.getIncentive(transaction);

        // Update balances (Task 4 logic)
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount + incentive);

        // Print Wilbur balance (for debugging / answer)
        if ("wilbur".equalsIgnoreCase(sender.getName())) {
           // System.out.println("WILBUR BALANCE: " + sender.getBalance());
        }
        if ("wilbur".equalsIgnoreCase(receiver.getName())) {
           // System.out.println("WILBUR BALANCE: " + receiver.getBalance());
        }

        // Save updated users
        userRepository.save(sender);
        userRepository.save(receiver);
    }
}