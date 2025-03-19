-- account Table
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance DOUBLE,
    version BIGINT
);

-- Transaction Table
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fromAccountId BIGINT,
    toAccountId BIGINT,
    amount DOUBLE,
    timestamp DATETIME,
    userId VARCHAR(255),
    idempotencyKey VARCHAR(255) UNIQUE,
    FOREIGN KEY (fromAccountId) REFERENCES account(id),
    FOREIGN KEY (toAccountId) REFERENCES account(id)
);

-- Audit Table
CREATE TABLE audit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accountId BIGINT,
    transactionType VARCHAR(255), -- DEBIT or CREDIT
    amount DOUBLE,
    timestamp DATETIME,
    userId VARCHAR(255),
    FOREIGN KEY (accountId) REFERENCES account(id)
);


-- Insert Records into account Table

INSERT INTO account (balance, version) VALUES (1000.00, 0);
INSERT INTO account (balance, version) VALUES (500.00, 0);
INSERT INTO account (balance, version) VALUES (2500.00, 0);
INSERT INTO account (balance, version) VALUES (750.00, 0);
INSERT INTO account (balance, version) VALUES (1500.00, 0);
INSERT INTO account (balance, version) VALUES (200.00, 0);
INSERT INTO account (balance, version) VALUES (3000.00, 0);
INSERT INTO account (balance, version) VALUES (900.00, 0);
INSERT INTO account (balance, version) VALUES (1200.00, 0);
INSERT INTO account (balance, version) VALUES (600.00, 0);

-- Optionally, you can add more records as needed.