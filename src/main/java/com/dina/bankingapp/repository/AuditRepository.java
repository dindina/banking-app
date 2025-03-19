package com.dina.bankingapp.repository;

import com.dina.bankingapp.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
