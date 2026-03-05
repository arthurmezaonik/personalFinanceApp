CREATE TABLE installment_plans(
    id UUID PRIMARY KEY,
    household_id UUID NOT NULL,
    provider VARCHAR(50) NOT NULL,
    total_amount DECIMAL(19, 4) NOT NULL,
    number_of_installments INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_installment_plan_household
        FOREIGN KEY (household_id)
            REFERENCES households(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_installment_plan_provider
        CHECK (provider IN ('KLARNA', 'CLEARPAY', 'OTHER')),

    CONSTRAINT chk_installment_plan_status
        CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED'))
);

CREATE INDEX idx_installment_plans_household
    ON installment_plans(household_id);
