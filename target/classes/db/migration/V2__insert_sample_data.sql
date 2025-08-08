INSERT INTO products (name, price, stock, category, is_active) VALUES
                                                                   ('Laptop', 1200.00, 10, 'Electronics', true),
                                                                   ('Phone', 800.00, 20, 'Electronics', true),
                                                                   ('Book', 20.00, 50, 'Education', true);

INSERT INTO orders (customer_name, customer_email, status, total_amount) VALUES
    ('John Doe', 'john@example.com', 'PENDING', 1000.00);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, total_price) VALUES
                                                                                      (1, 1, 1, 1200.00, 1200.00),
                                                                                      (1, 2, 1, 800.00, 800.00);
