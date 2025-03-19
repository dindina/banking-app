package com.dina.banking_app.controller;

import com.dina.banking_app.dto.TransferRequest;
import com.dina.banking_app.entity.Account;
import com.dina.banking_app.repository.AccountRepository;
import com.dina.banking_app.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping
    public void transferFunds(@RequestBody TransferRequest transferRequest) {

        List<Account> accountList = accountRepository.findAll();
        System.out.println("all records " + accountList);
        transferService.transferFunds(transferRequest);
    }
}
