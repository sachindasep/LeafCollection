/*CREATE TABLE IF NOT EXISTS branch (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS branch_rate (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             branch_id BIGINT NOT NULL,
                             rate_month INT NOT NULL,
                             rate_year INT NOT NULL,
                             rate_per_kg DECIMAL(10,2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT unique_branch_month UNIQUE(branch_id, rate_month, rate_year),
                             FOREIGN KEY (branch_id) REFERENCES branch(id)
);

CREATE TABLE IF NOT EXISTS party (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       phone VARCHAR(20),
                       branch_id BIGINT NOT NULL,
                       opening_balance DECIMAL(12,2) DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (branch_id) REFERENCES branch(id)
);

CREATE TABLE IF NOT EXISTS leaf_entry (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            party_id BIGINT NOT NULL,
                            entry_date DATE NOT NULL,
                            quantity DECIMAL(12,2) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (party_id) REFERENCES party(id)
);

CREATE TABLE IF NOT EXISTS payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         party_id BIGINT NOT NULL,
                         payment_date DATE NOT NULL,
                         amount DECIMAL(12,2) NOT NULL,
                         payment_type VARCHAR(20),
                         notes VARCHAR(255),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (party_id) REFERENCES party(id)
);

CREATE TABLE IF NOT EXISTS monthly_bill (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              party_id BIGINT NOT NULL,
                              bill_month INT NOT NULL,
                              bill_year INT NOT NULL,
                              total_quantity DECIMAL(12,2) NOT NULL,
                              rate DECIMAL(10,2) NOT NULL,
                              total_amount DECIMAL(12,2) NOT NULL,
                              advance_paid DECIMAL(12,2) DEFAULT 0,
                              balance DECIMAL(12,2) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT unique_party_month UNIQUE(party_id, bill_month, bill_year),
                              FOREIGN KEY (party_id) REFERENCES party(id)
);*/