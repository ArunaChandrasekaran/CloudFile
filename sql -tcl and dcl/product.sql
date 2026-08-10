CREATE TABLE product (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100),
    price DECIMAL(10,2)
);

INSERT INTO product
VALUES
(1, 'Laptop', 55000),
(2, 'Mobile', 25000),
(3, 'Headphones', 2000);


CREATE OR REPLACE PROCEDURE view_menu(choice INT)
LANGUAGE plpgsql
AS $$
BEGIN

    IF choice = 1 THEN

        CREATE OR REPLACE VIEW product_view AS
        SELECT * FROM product;

        RAISE NOTICE 'View created successfully';

    ELSIF choice = 2 THEN

        RAISE NOTICE 'Displaying data from view';

        -- Use SELECT separately:
        -- SELECT * FROM product_view;

    ELSIF choice = 3 THEN

        UPDATE product_view
        SET price = price + 500
        WHERE product_id = 1;

        RAISE NOTICE 'Data updated successfully';

    ELSIF choice = 4 THEN

        DROP VIEW IF EXISTS product_view;

        RAISE NOTICE 'View dropped successfully';

    ELSIF choice = 5 THEN

        RAISE NOTICE 'Program exited';

    ELSE

        RAISE NOTICE 'Invalid choice';

    END IF;

END;
$$;



CALL view_menu(1);


CALL view_menu(4);