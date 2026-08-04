CREATE TABLE employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    salary DECIMAL(10,2)
);



CREATE OR REPLACE FUNCTION before_insert_employee()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.salary < 10000 THEN
        NEW.salary := 10000;
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_before_insert
BEFORE INSERT ON employee
FOR EACH ROW
EXECUTE FUNCTION before_insert_employee();


CREATE OR REPLACE FUNCTION before_update_employee()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.salary < 10000 THEN
        RAISE EXCEPTION 'Salary cannot be less than 10000';
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_before_update
BEFORE UPDATE ON employee
FOR EACH ROW
EXECUTE FUNCTION before_update_employee();


CREATE OR REPLACE PROCEDURE employee_menu(
    IN p_choice INT,
    IN p_id INT,
    IN p_name VARCHAR(50),
    IN p_salary NUMERIC(10,2)
)
LANGUAGE plpgsql
AS
$$
BEGIN

    CASE p_choice

        WHEN 1 THEN
            INSERT INTO employee(emp_id, emp_name, salary)
            VALUES (p_id, p_name, p_salary);

            RAISE NOTICE 'Employee inserted successfully';

        WHEN 2 THEN
            UPDATE employee
            SET salary = p_salary
            WHERE emp_id = p_id;

            RAISE NOTICE 'Salary updated successfully';

        WHEN 3 THEN
            DELETE FROM employee
            WHERE emp_id = p_id;

            RAISE NOTICE 'Employee deleted successfully';

        WHEN 4 THEN
            RAISE NOTICE 'Employee Records:';

        WHEN 5 THEN
            RAISE NOTICE 'Program Exited';

        ELSE
            RAISE NOTICE 'Invalid Choice';

    END CASE;
END;
$$;

SELECT * FROM employee;


CALL employee_menu(
1,
101,
'Aruna',
8000
);

CALL employee_menu(
2,
101,
NULL,
15000
);



