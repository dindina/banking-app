package com.dina.banking_app.service;

import com.dina.banking_app.dto.TransferRequest;
import com.dina.banking_app.entity.Account;
import com.dina.banking_app.entity.Audit;
import com.dina.banking_app.entity.Transaction;
import com.dina.banking_app.repository.AccountRepository;
import com.dina.banking_app.repository.AuditRepository;
import com.dina.banking_app.repository.TransactionRepository;
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
            List<Account> accountList = accountRepository.findAll();
            System.out.println(accountList);

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
