package com.dina.bankingapp.entity;

import jakarta.persistence.*;
/*
The @Version field in your JPA entity class (Account) is updated automatically after a successful transaction that modifies the entity because it's a core part of JPA's optimistic locking mechanism. Here's a breakdown of why this happens:

Purpose of @Version and Optimistic Locking:

Preventing Lost Updates: In concurrent environments where multiple transactions might try to update the same record, optimistic locking aims to prevent the "lost update" problem. This occurs when one transaction overwrites changes made by another transaction without being aware of those changes.
How it Works:
Read: When a transaction reads an Account entity, the value of the @Version field is also read and stored.
Update: When the transaction attempts to update the Account entity, the JPA provider includes the original version value in the WHERE clause of the update statement. For example:
SQL

UPDATE account
SET balance = ?, version = ?
WHERE id = ? AND version = ?
The version = ? in the WHERE clause checks if the version of the record in the database is still the same as the version that the transaction read.
Success: If the versions match, it means no other transaction has modified the record since it was read. The update proceeds, and the JPA provider automatically increments the @Version field in the database as part of the same transaction. The updated entity in your application context will also reflect this new version.  
Conflict (OptimisticLockException): If the versions do not match, it means another transaction has already updated the record. The WHERE clause in the update statement will not find any matching rows to update. In this case, the JPA provider will throw an OptimisticLockException, indicating a concurrency conflict. The current transaction will typically be rolled back, and the user (or the application logic) needs to handle this conflict (e.g., by retrying the operation with the latest data).  
Why Automatic Update?

The automatic increment of the @Version field is essential for the optimistic locking mechanism to work correctly:

Tracking Changes: Incrementing the version number acts as a simple flag indicating that the entity has been modified.
Ensuring Future Conflict Detection: When subsequent transactions try to update the same entity, the now-incremented version in the database will not match the older version that those transactions might have read, thus triggering the OptimisticLockException if they attempt to commit their changes.
JPA Provider Responsibility: The JPA provider (like Hibernate or EclipseLink) is responsible for managing the @Version field automatically. You, as the developer, should never manually modify the value of the @Version field. Letting the JPA provider handle it ensures the integrity of the optimistic locking process.
 */

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private double balance;

    @Version
    @Column(name = "version")
    private Long version; // Optimistic locking field , see details above

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}