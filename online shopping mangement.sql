CREATE TABLE product (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100),
    price DECIMAL(10,2),
    stock INT
);

INSERT INTO product (product_id, product_name, price, stock)
VALUES
(101, 'Laptop', 55000.00, 10),
(102, 'Mobile Phone', 25000.00, 15),
(103, 'Headphones', 2000.00, 20),
(104, 'Keyboard', 1500.00, 25),
(105, 'Mouse', 800.00, 30);

CREATE TABLE customer (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(100),
    email VARCHAR(100)
);

INSERT INTO customer (customer_id, customer_name, email)
VALUES
(1, 'Arun', 'arun@gmail.com'),
(2, 'Karthik', 'karthik@gmail.com'),
(3, 'Priya', 'priya@gmail.com');


UPDATE product
SET stock = stock - 1
WHERE product_id = 101
AND stock > 0;

SELECT * FROM product
WHERE product_id = 101;

COMMIT;

ROLLBACK;

CREATE VIEW product_view AS
SELECT product_id, product_name, price, stock
FROM product;

SELECT * FROM product_view;

CREATE USER customer_user WITH PASSWORD 'customer123';

GRANT SELECT ON product_view TO customer_user;

SELECT * FROM product_view;
