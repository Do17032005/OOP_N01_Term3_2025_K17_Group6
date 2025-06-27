-- Sample users for cinema booking system
-- Password for all users: "password123" (BCrypt encoded)

INSERT INTO users (username, password, email, full_name, phone_number, user_role) VALUES
-- Admin account
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@cinema.com', 'System Administrator', '0123456789', 'ADMIN'),

-- Customer accounts
('john_doe', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'john@example.com', 'John Doe', '0123456789', 'CUSTOMER'),
('jane_smith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'jane@example.com', 'Jane Smith', '0987654321', 'CUSTOMER'),
('bob_wilson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'bob@example.com', 'Bob Wilson', '0555666777', 'CUSTOMER'),
('alice_brown', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'alice@example.com', 'Alice Brown', '0111222333', 'CUSTOMER'),
('charlie_davis', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'charlie@example.com', 'Charlie Davis', '0444555666', 'CUSTOMER');

-- Note: All passwords are "password123" encoded with BCrypt
-- You can use any of these accounts to login:
-- Admin: admin@cinema.com / password123
-- Customers: john@example.com, jane@example.com, bob@example.com, alice@example.com, charlie@example.com / password123 