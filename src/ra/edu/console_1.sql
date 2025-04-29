create database manager_phone_store;

use manager_phone_store;

create table account(
                        account_id int auto_increment primary key,
                        username varchar(50) not null unique,
                        password varchar(255) not null
);

insert into account(username, password)
values ('vuong1511', 'vuong12345');

create table products(
                         productid int auto_increment primary key,
                         name varchar(100) not null unique,
                         brand varchar(50),
                         price decimal(12,2) not null,
                         stock int default 0 check (stock >= 0),
                         status boolean default true
);

insert into products(name, brand, price, stock)
values ('Iphone 14', 'Apple', 3000, 15),
       ('Iphone 15', 'Apple', 2000, 15),
       ('SamSung s24 ultra', 'SamSung', 3200, 15),
       ('Redmi note 14 pro 5G', 'Xiaomi', 8700, 15);

create table customers(
                          customer_id int auto_increment primary key,
                          name varchar(100) not null,
                          phone varchar(20),
                          email varchar(100) unique,
                          address varchar(255),
                          status boolean default true
);

INSERT INTO customers (name, phone, email, address)
VALUES ('Vuong', '0123456789', 'Vuong@gmail.com', 'Ha Noi'),
       ('Vu', '0987651234', 'Vu123@gmail.com', 'Hai Duong');

create table invoices(
                         invoice_id int auto_increment primary key,
                         customer_id int,
                         foreign key (customer_id) references customers(customer_id),
                         invoice_date datetime default(current_timestamp),
                         total decimal(12,2) not null check (total >= 0)
);

create table invoice_items(
                              item_id int auto_increment primary key,
                              invoice_id int,
                              productid int,
                              quantity int not null check (quantity > 0),
                              unit_price decimal(12,2) not null check (unit_price >= 0),
                              foreign key (invoice_id) references invoices(invoice_id),
                              foreign key (productid) references products(productid)
);

DELIMITER //

CREATE PROCEDURE admin_login(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    OUT p_account_id INT
)
BEGIN
    SELECT account_id INTO p_account_id
    FROM account
    WHERE username = p_username AND password = p_password;

    IF p_account_id IS NULL THEN
        SET p_account_id = 0;
    END IF;
END //

CREATE PROCEDURE get_phone_list()
BEGIN
    SELECT productid, name, price, brand, stock, status FROM Products
    WHERE status = TRUE AND stock > 0
    ORDER BY name;
END //

CREATE PROCEDURE add_phone(
    IN p_name VARCHAR(100),
    IN p_price DECIMAL(12,2),
    IN p_brand VARCHAR(50),
    IN p_stock INT
)
BEGIN
    INSERT INTO Products (name, price, brand, stock)
    VALUES (p_name, p_price, p_brand, p_stock);
    SELECT LAST_INSERT_ID() AS new_product_id;
END //

CREATE PROCEDURE update_phone(
    IN p_product_id INT,
    IN p_name VARCHAR(100),
    IN p_price DECIMAL(12,2),
    IN p_brand VARCHAR(50),
    IN p_stock INT
)
BEGIN
    DECLARE affected_rows INT;
    UPDATE Products
    SET name = p_name, price = p_price, brand = p_brand, stock = p_stock
    WHERE productid = p_product_id AND status = TRUE;
    SET affected_rows = ROW_COUNT();
    IF affected_rows = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Sản phẩm không tồn tại';
    END IF;
END //

CREATE PROCEDURE delete_phone(
    IN p_product_id INT
)
BEGIN
    DECLARE affected_rows INT;
    UPDATE products
    SET status = FALSE
    WHERE productid = p_product_id AND status = TRUE;
    SET affected_rows = ROW_COUNT();
    IF affected_rows = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Sản phẩm không tồn tại';
    END IF;
END //

CREATE PROCEDURE find_phone_by_brand(
    IN p_brand VARCHAR(50)
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE brand LIKE CONCAT('%', p_brand, '%') AND status = TRUE;
END //

CREATE PROCEDURE find_phone_by_price_range(
    IN p_min_price DECIMAL(12,2),
    IN p_max_price DECIMAL(12,2)
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE price BETWEEN p_min_price AND p_max_price AND status = TRUE;
END //

CREATE PROCEDURE find_phone_by_stock(
    IN p_min_stock INT,
    IN p_max_stock INT
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE stock BETWEEN p_min_stock AND p_max_stock AND status = TRUE;
END //

CREATE PROCEDURE get_customer_list()
BEGIN
    SELECT customer_id, name, email, phone, address FROM Customers
    WHERE status = TRUE
    ORDER BY name;
END //

CREATE PROCEDURE add_customer(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_address VARCHAR(255)
)
BEGIN
    INSERT INTO Customers (name, email, phone, address)
    VALUES (p_name, p_email, p_phone, p_address);
    SELECT LAST_INSERT_ID() AS new_customer_id;
END //

CREATE PROCEDURE update_customer(
    IN p_customer_id INT,
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_address VARCHAR(255)
)
BEGIN
    DECLARE affected_rows INT;
    UPDATE Customers
    SET name = p_name, email = p_email, phone = p_phone, address = p_address
    WHERE customer_id = p_customer_id AND status = TRUE;
    SET affected_rows = ROW_COUNT();
    IF affected_rows = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Khách hàng không tồn tại';
    END IF;
END //

CREATE PROCEDURE delete_customer(
    IN p_customer_id INT
)
BEGIN
    DECLARE affected_rows INT;
    UPDATE Customers
    SET status = FALSE
    WHERE customer_id = p_customer_id AND status = TRUE;
    SET affected_rows = ROW_COUNT();
    IF affected_rows = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Khách hàng không tồn tại';
    END IF;
END //

CREATE PROCEDURE get_invoices()
BEGIN
    SELECT i.invoice_id, i.customer_id, i.invoice_date, i.total
    FROM Invoices i
    ORDER BY i.invoice_date DESC;
END //

CREATE PROCEDURE add_invoice(
    IN p_customer_id INT,
    IN p_total_amount DECIMAL(12,2)
)
BEGIN
    DECLARE customer_exists INT;
    SELECT COUNT(*) INTO customer_exists
    FROM customers
    WHERE customer_id = p_customer_id AND status = TRUE;
    IF customer_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Khách hàng không tồn tại';
    END IF;
    INSERT INTO Invoices (customer_id, total)
    VALUES (p_customer_id, p_total_amount);
    SELECT LAST_INSERT_ID() AS new_invoice_id;
END //

CREATE PROCEDURE add_invoice_item(
    IN p_invoice_id INT,
    IN p_product_id INT,
    IN p_quantity INT,
    IN p_unit_price DECIMAL(12,2)
)
BEGIN
    DECLARE current_stock INT;
    DECLARE invoice_exists INT;
    SELECT COUNT(*) INTO invoice_exists
    FROM invoices
    WHERE invoice_id = p_invoice_id;
    IF invoice_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Hóa đơn không tồn tại';
    END IF;
    SELECT stock INTO current_stock
    FROM products
    WHERE productid = p_product_id AND status = TRUE;
    IF current_stock IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Sản phẩm không tồn tại';
    END IF;
    IF current_stock < p_quantity THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Số lượng không đủ';
    END IF;
    INSERT INTO invoice_items (invoice_id, productid, quantity, unit_price)
    VALUES (p_invoice_id, p_product_id, p_quantity, p_unit_price);
    UPDATE products
    SET stock = stock - p_quantity
    WHERE productid = p_product_id;
END //

CREATE PROCEDURE search_invoices_by_customer_name(
    IN p_customer_name VARCHAR(100)
)
BEGIN
    SELECT i.invoice_id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total
    FROM Invoices i
             JOIN Customers c ON i.customer_id = c.customer_id
    WHERE c.name LIKE CONCAT('%', p_customer_name, '%') AND c.status = TRUE;
END //

CREATE PROCEDURE search_invoices_by_date(
    IN p_date DATE
)
BEGIN
    SELECT i.invoice_id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total
    FROM Invoices i
             JOIN Customers c ON i.customer_id = c.customer_id
    WHERE i.invoice_date >= p_date AND i.invoice_date < DATE_ADD(p_date, INTERVAL 1 DAY)
      AND c.status = TRUE;
END //

DELIMITER ;