CREATE TABLE vehicle_details (
    vehicle_id INT PRIMARY KEY,
    vehicle_name VARCHAR(100),
    vehicle_number VARCHAR(50),
    capacity INT
);


INSERT INTO vehicle_details
VALUES
(1, 'Tata Truck', 'TN58AB1234', 10),
(2, 'Ashok Leyland', 'TN59CD5678', 15),
(3, 'BharatBenz', 'TN60EF9012', 20);

CREATE TABLE delivery_details (
    delivery_id INT PRIMARY KEY,
    product_name VARCHAR(100),
    shop_name VARCHAR(100),
    delivery_area VARCHAR(100),
    status VARCHAR(50)
);


BEGIN;

INSERT INTO delivery_details
VALUES
(101, 'Rice Bags', 'ABC Stores', 'Madurai', 'Completed'),
(102, 'Electronics', 'XYZ Mart', 'Chennai', 'Pending'),
(103, 'Furniture', 'Home Needs', 'Trichy', 'Completed');

COMMIT;

SELECT * FROM delivery_details;

CREATE VIEW completed_deliveries AS
SELECT *
FROM delivery_details
WHERE status = 'Completed';


SELECT * FROM completed_deliveries;


CREATE INDEX idx_delivery_area
ON delivery_details(delivery_area);


SELECT *
FROM delivery_details
WHERE delivery_area = 'Madurai';

BEGIN;

DELETE FROM delivery_details
WHERE delivery_id = 102;

SELECT * FROM delivery_details;

ROLLBACK;

CREATE USER transport_user
WITH PASSWORD 'transport123';


GRANT SELECT
ON delivery_details
TO transport_user;

SELECT * FROM delivery_details;

DELETE FROM delivery_details;