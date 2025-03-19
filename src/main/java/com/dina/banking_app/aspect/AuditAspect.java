package com.dina.banking_app.aspect;

import com.dina.banking_app.dto.TransferRequest;
import com.dina.banking_app.entity.Audit;
import com.dina.banking_app.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditRepository auditRepository;

    @Pointcut("execution(* com.dina.banking_app.service.TransferService.transferFunds(..))")
    public void transferFundsPointcut() {}

    @AfterReturning(pointcut = "transferFundsPointcut()", returning = "result")
    public void auditTransfer(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        TransferRequest transferRequest = (TransferRequest) args[0];

        // Create Audit Record for Debit
        Audit debitAudit = new Audit();
        debitAudit.setAccountId(transferRequest.getFromAccountId());
        debitAudit.setTransactionType("DEBIT");
        debitAudit.setAmount(transferRequest.getAmount());
        debitAudit.setTimestamp(LocalDateTime.now());
        debitAudit.setUserId(transferRequest.getUserId());
        auditRepository.save(debitAudit);

        // Create Audit Record for Credit
        Audit creditAudit = new Audit();
        creditAudit.setAccountId(transferRequest.getToAccountId());
        creditAudit.setTransactionType("CREDIT");
        creditAudit.setAmount(transferRequest.getAmount());
        creditAudit.setTimestamp(LocalDateTime.now());
        creditAudit.setUserId(transferRequest.getUserId());
        auditRepository.save(creditAudit);
    }
}