-- Products
INSERT INTO products (id, name, price, stock, category, is_active, created_at) VALUES
(1, 'Laptop', 1200.00, 50, 'Electronics', true, CURRENT_TIMESTAMP),
(2, 'Smartphone', 800.00, 150, 'Electronics', true, CURRENT_TIMESTAMP),
(3, 'T-shirt', 25.00, 500, 'Apparel', true, CURRENT_TIMESTAMP),
(4, 'Book: The Pragmatic Programmer', 45.00, 100, 'Books', true, CURRENT_TIMESTAMP);

-- Orders
INSERT INTO orders (id, customer_name, customer_email, order_date, status, total_amount) VALUES
(1, 'John Doe', 'john.doe@example.com', CURRENT_TIMESTAMP, 'PENDING', 1225.00),
(2, 'Jane Smith', 'jane.smith@example.com', CURRENT_TIMESTAMP, 'CONFIRMED', 845.00);

-- Order Items
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, total_price) VALUES
(1, 1, 1, 1, 1200.00, 1200.00),
(2, 1, 3, 1, 25.00, 25.00),
(3, 2, 2, 1, 800.00, 800.00),
(4, 2, 4, 1, 45.00, 45.00);