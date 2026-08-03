-- Function to calculate total marks
CREATE OR REPLACE FUNCTION total_marks(mark1 INT, mark2 INT, mark3 INT)
RETURNS INT
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN mark1 + mark2 + mark3;
END;
$$;

-- Procedure to display total marks and result
CREATE OR REPLACE PROCEDURE student_result(mark1 INT, mark2 INT, mark3 INT)
LANGUAGE plpgsql
AS $$
DECLARE
    total INT;
BEGIN
    -- Call the function
    total := total_marks(mark1, mark2, mark3);

    -- Display total marks
    RAISE NOTICE 'Total Marks: %', total;

    -- Display result
    IF total >= 150 THEN
        RAISE NOTICE 'Result: PASS';
    ELSE
        RAISE NOTICE 'Result: FAIL';
    END IF;
END;
$$;

-- Execute the procedure
CALL student_result(60, 55, 50);





-- Function to calculate Simple Interest
CREATE OR REPLACE FUNCTION simple_interest(p NUMERIC, r NUMERIC, t NUMERIC)
RETURNS NUMERIC
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN (p * r * t) / 100;
END;
$$;

-- Procedure to display Principal, SI, and Total Amount
CREATE OR REPLACE PROCEDURE interest_details(
    p NUMERIC,
    r NUMERIC,
    t NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    si NUMERIC;
    total NUMERIC;
BEGIN
    -- Call the function
    si := simple_interest(p, r, t);

    -- Calculate total amount
    total := p + si;

    -- Display results
    RAISE NOTICE 'Principal Amount : %', p;
    RAISE NOTICE 'Simple Interest  : %', si;
    RAISE NOTICE 'Total Amount     : %', total;
END;
$$;

-- Execute the procedure
CALL interest_details(10000, 5, 2);