-- products
INSERT INTO products (id, name, price, stock, category, visibility, created_at)
VALUES
    (1, 'Laptop Dell XPS 13', 1500.00, 10, 'Electronics', TRUE, CURRENT_TIMESTAMP),
    (2, 'iPhone 14 Pro', 1200.00, 15, 'Electronics', TRUE, CURRENT_TIMESTAMP),
    (3, 'Nike Air Max', 200.00, 25, 'Fashion', TRUE, CURRENT_TIMESTAMP),
    (4, 'Samsung Galaxy S23', 1100.00, 20, 'Electronics', TRUE, CURRENT_TIMESTAMP);

-- orders
INSERT INTO orders (id, customer_name, customer_email, order_date, status, total_amount, visibility, created_at)
VALUES
    (1, 'John Doe', 'john@example.com', CURRENT_TIMESTAMP, 'PENDING', 2700.00, TRUE, CURRENT_TIMESTAMP),
    (2, 'Alice Smith', 'alice@example.com', CURRENT_TIMESTAMP, 'CONFIRMED', 200.00, TRUE, CURRENT_TIMESTAMP);

-- order_items
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, total_price, visibility, created_at)
VALUES
    (1, 1, 1, 1, 1500.00, 1500.00, TRUE, CURRENT_TIMESTAMP),
    (2, 1, 2, 1, 1200.00, 1200.00, TRUE, CURRENT_TIMESTAMP),
    (3, 2, 3, 1, 200.00, 200.00, TRUE, CURRENT_TIMESTAMP);
