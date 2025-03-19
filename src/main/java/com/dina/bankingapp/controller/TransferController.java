package com.dina.bankingapp.controller;

import com.dina.bankingapp.dto.TransferRequest;
import com.dina.bankingapp.entity.Account;
import com.dina.bankingapp.repository.AccountRepository;
import com.dina.bankingapp.service.TransferService;
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

        transferService.transferFunds(transferRequest);
    }
}
