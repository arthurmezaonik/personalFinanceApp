CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(120) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(60) NOT NULL,
    household_id UUID NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_household
        FOREIGN KEY (household_id)
        REFERENCES households(id)
        ON DELETE CASCADE
);
