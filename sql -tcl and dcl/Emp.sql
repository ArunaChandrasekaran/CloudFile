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