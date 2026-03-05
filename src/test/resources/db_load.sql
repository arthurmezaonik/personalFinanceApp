-- Household
INSERT INTO households (id, name, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Test Household', NOW());
INSERT INTO households (id, name, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440010', 'Test Household To Delete', NOW());

-- User
INSERT INTO users (id, first_name, last_name, email, password, role, household_id, active, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'John', 'Doe', 'john.doe@example.com', 'password', 'ADMIN', '550e8400-e29b-41d4-a716-446655440000', TRUE, NOW());

-- Payment Method
INSERT INTO payment_methods (id, household_id, name, type, active, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', 'Checking Account', 'DEBIT', TRUE, NOW());

-- Tag
INSERT INTO tags (id, household_id, name, color, active, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'Grocery', '#FF0000', TRUE, NOW());

-- Savings Account
INSERT INTO savings_accounts (id, household_id, name, active, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000', 'Emergency Fund', TRUE, NOW());

-- Movement
INSERT INTO movements (id, household_id, amount, direction, status, date, payment_method_id, tag_id, created_by, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440000', 50.00, 'EXPENSE', 'COMPLETED', CURRENT_DATE, '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', NOW());
