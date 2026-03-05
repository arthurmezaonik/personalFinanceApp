CREATE TABLE tags(
    id UUID PRIMARY KEY,
    household_id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    color VARCHAR (20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tag_household
        FOREIGN KEY (household_id)
            REFERENCES households(id)
            ON DELETE CASCADE
);

CREATE INDEX idx_tag_household
    ON tags(household_id);