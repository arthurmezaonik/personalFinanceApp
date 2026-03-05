CREATE TABLE movements(
    id UUID PRIMARY KEY,
    household_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    direction VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    date DATE NOT NULL,
    original_date DATE,
    payment_method_id UUID NOT NULL,
    installment_plan_id UUID,
    tag_id UUID,
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_by UUID,

    CONSTRAINT fk_movement_household
        FOREIGN KEY (household_id)
            REFERENCES households(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_movement_payment_method
        FOREIGN KEY (payment_method_id)
            REFERENCES payment_methods(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_movement_installment_plan
        FOREIGN KEY (installment_plan_id)
            REFERENCES installment_plans(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_movement_tag
        FOREIGN KEY (tag_id)
            REFERENCES tags(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_movement_created_by
        FOREIGN KEY (created_by)
            REFERENCES users(id),

    CONSTRAINT fk_movement_deleted_by
        FOREIGN KEY (deleted_by)
            REFERENCES users(id)
);

CREATE INDEX idx_movement_household_date ON movements(household_id, date);
CREATE INDEX idx_movement_household_status ON movements(household_id, status);
CREATE INDEX idx_movement_household_direction ON movements(household_id, direction);
CREATE INDEX idx_movement_installment_plan ON movements(installment_plan_id);