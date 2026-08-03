CREATE TABLE attendance (
    attendance_id SERIAL PRIMARY KEY,
    emp_id INT,
    attendance_date DATE,
    status TEXT
);

CREATE OR REPLACE PROCEDURE count_absent_days()
LANGUAGE plpgsql
AS $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN
        SELECT emp_id, COUNT(*) AS absent_days
        FROM attendance
        WHERE status = 'Absent'
        GROUP BY emp_id
    LOOP
        RAISE NOTICE 'Employee ID: %, Absent Days: %',
                     rec.emp_id, rec.absent_days;
    END LOOP;
END;
$$;

CALL count_absent_days();

CREATE OR REPLACE FUNCTION update_attendance()
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
DECLARE
    updated_count INT;
BEGIN
    UPDATE attendance
    SET status = 'Present'
    WHERE status = 'Late';

    GET DIAGNOSTICS updated_count = ROW_COUNT;

    RETURN updated_count;
END;
$$;

SELECT update_attendance();


SELECT
    emp_id,
    COUNT(*) FILTER (WHERE status = 'Absent') AS absent_days,
    CASE
        WHEN COUNT(*) FILTER (WHERE status = 'Absent') <= 2
            THEN 'Good'
        WHEN COUNT(*) FILTER (WHERE status = 'Absent') BETWEEN 3 AND 5
            THEN 'Average'
        ELSE 'Poor'
    END AS attendance_summary
FROM attendance
GROUP BY emp_id;







CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title TEXT,
    author TEXT,
    copies INT
);


CREATE OR REPLACE FUNCTION issue_book(b_id INT)
RETURNS TEXT
LANGUAGE plpgsql
AS $$
DECLARE
    c INT;
BEGIN
    SELECT copies INTO c
    FROM books
    WHERE book_id = b_id;

    IF c > 0 THEN
        UPDATE books
        SET copies = copies - 1
        WHERE book_id = b_id;

        RETURN 'Book Issued';
    ELSE
        RETURN 'Book Not Available';
    END IF;
END;
$$;


SELECT issue_book(1);

ALTER TABLE books
ADD COLUMN status TEXT;


CREATE OR REPLACE PROCEDURE update_book_status()
LANGUAGE plpgsql
AS $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN
        SELECT book_id, copies
        FROM books
    LOOP
        IF rec.copies = 0 THEN
            UPDATE books
            SET status = 'Out of Stock'
            WHERE book_id = rec.book_id;
        END IF;
    END LOOP;
END;
$$;


CALL update_book_status();


SELECT
    book_id,
    title,
    copies,
    CASE
        WHEN copies > 5 THEN 'Available'
        WHEN copies BETWEEN 1 AND 5 THEN 'Limited'
        ELSE 'Out of Stock'
    END AS availability_status
FROM books;


CREATE OR REPLACE FUNCTION check_book_availability(b_id INT)
RETURNS TEXT
LANGUAGE plpgsql
AS $$
DECLARE
    c INT;
BEGIN
    SELECT copies INTO c
    FROM books
    WHERE book_id = b_id;

    IF c > 0 THEN
        RETURN 'Available';
    ELSE
        RETURN 'Not Available';
    END IF;
END;

$$;

SELECT check_book_availability(1);