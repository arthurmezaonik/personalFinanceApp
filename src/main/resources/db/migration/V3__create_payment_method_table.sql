CREATE TABLE payment_methods (
    id UUID PRIMARY KEY,
    household_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_payment_methods_household
        FOREIGN KEY (household_id)
            REFERENCES households(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_payment_method_type
        CHECK (type IN ('CASH','DEBIT','CREDIT','BNPL'))
);

CREATE INDEX idx_payment_methods_household
    ON payment_methods(household_id);

CREATE INDEX idx_payment_methods_household_active
    ON payment_methods(household_id, active);