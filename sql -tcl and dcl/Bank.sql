CREATE TABLE bank_account (
    account_id INT PRIMARY KEY,
    account_holder VARCHAR(100),
    balance DECIMAL(10,2)
);

INSERT INTO bank_account
VALUES
(101, 'Arun', 50000),
(102, 'Karthik', 30000),
(103, 'Priya', 45000);

SELECT * FROM bank_account;


UPDATE bank_account
SET balance = balance + 5000
WHERE account_id = 101;

SELECT * FROM bank_account;

ROLLBACK;


CREATE TABLE employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(100),
    salary DECIMAL(10,2)
);

INSERT INTO employee
VALUES
(1, 'Arun', 30000),
(2, 'Karthik', 35000),
(3, 'Priya', 40000);


CREATE VIEW employee_name_view AS
SELECT emp_name
FROM employee;

SELECT * FROM employee_name_view;


CREATE USER guest WITH PASSWORD 'guest123';

GRANT SELECT ON employee_name_view TO guest;


SELECT * FROM employee_name_view;