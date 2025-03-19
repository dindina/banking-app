package com.dina.bankingapp.service;

import com.dina.bankingapp.dto.TransferRequest;
import com.dina.bankingapp.entity.Account;
import com.dina.bankingapp.entity.Transaction;
import com.dina.bankingapp.repository.AccountRepository;
import com.dina.bankingapp.repository.AuditRepository;
import com.dina.bankingapp.repository.TransactionRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuditRepository auditRepository;


    @Transactional
    public void transferFunds(TransferRequest transferRequest) {
        if (transactionRepository.existsByIdempotencyKey(transferRequest.getIdempotencyKey())) {
            // Transaction already processed
            return;
        }

        try {
            Account fromAccount = accountRepository.findById(transferRequest.getFromAccountId())
                    .orElseThrow(() -> new RuntimeException("From account not found"));
            Account toAccount = accountRepository.findById(transferRequest.getToAccountId())
                    .orElseThrow(() -> new RuntimeException("To account not found"));

            if (fromAccount.getBalance() < transferRequest.getAmount()) {
                throw new RuntimeException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance() - transferRequest.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transferRequest.getAmount());

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Create Transaction Record
            Transaction transaction = new Transaction();
            transaction.setFromAccountId(transferRequest.getFromAccountId());
            transaction.setToAccountId(transferRequest.getToAccountId());
            transaction.setAmount(transferRequest.getAmount());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setUserId(transferRequest.getUserId());
            transaction.setIdempotencyKey(transferRequest.getIdempotencyKey());
            transactionRepository.save(transaction);


        } catch (OptimisticLockException e) {
            // Handle optimistic locking conflict (e.g., retry, inform user)
            throw new RuntimeException("Transaction conflict. Please try again.");
        }
    }
}
