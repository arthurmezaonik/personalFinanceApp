CREATE TABLE savings_accounts(
                     id UUID PRIMARY KEY,
                     household_id UUID NOT NULL,
                     name VARCHAR(50) NOT NULL,
                     active BOOLEAN NOT NULL DEFAULT TRUE,
                     created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

                     CONSTRAINT fk_savings_account_household
                         FOREIGN KEY (household_id)
                             REFERENCES households(id)
                             ON DELETE CASCADE
);

CREATE INDEX idx_savings_account_household
    ON savings_accounts(household_id);