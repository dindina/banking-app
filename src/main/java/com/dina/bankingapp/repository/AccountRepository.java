package com.dina.bankingapp.repository;

import com.dina.bankingapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
/*import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdForUpdate(Long id);

    // You can add other custom queries here if needed
}*/

public interface AccountRepository extends JpaRepository<Account, Long> {
    // Remove findByIdForUpdate() method
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findById(Long id);
}
