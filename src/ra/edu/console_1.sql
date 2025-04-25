create database manager_phone_store;

use manager_phone_store;

create table account(
    account_id int auto_increment primary key ,
    username varchar(50) not null,
    password varchar(50) not null
);

insert into account(username, password)
values ('vuong1511', 'vuong12345');

create table products(
     productid int auto_increment primary key,
     name varchar(100) not null ,
     brand varchar(50) ,
     price decimal(12,2) not null ,
     stock int default 0,
     status bit default(1)
);
;
insert into products(name, brand, price, stock)
values ('Iphone 14', 'Apple', 3000, 15 ),
       ('Iphone 15', 'Apple', 2000, 15),
       ('SamSung s24 ultra', 'SamSung', 3200, 15),
       ('Redmi note 14 pro 5G', 'Xiaomi', 8700, 15);

create table customers(
    customer_id int auto_increment primary key ,
    name varchar(100) not null ,
    phone varchar(20),
    email varchar(100) unique ,
    address varchar(255),
    status bit default(1)
);

INSERT INTO customers (name, phone, email, address )
VALUES ('Vuong', '0123456789' ,'Vuong@gmail.com', 'Ha Noi'),
       ('Vu', '0987651234', 'Vu@gmail.com', 'Hai Duong');

create table invoices(
    invoice_id int auto_increment primary key ,
    customer_id int,
    foreign key (customer_id) references customers(customer_id),
    invoice_date datetime default(current_timestamp),
    total decimal(12,2)
);

create table invoice_items(
    item_id int auto_increment primary key,
    invoice_id int,
    productid int,
    quantity int not null ,
    unit_price decimal(12,2) not null ,
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

DELIMITER ;



DELIMITER //

-- Lấy danh sách điện thoại
CREATE PROCEDURE get_phone_list()
BEGIN
    SELECT productid, name, price, brand, stock, status FROM Products
    WHERE status = TRUE AND stock > 0
    ORDER BY name;
END //

-- Thêm điện thoại
CREATE PROCEDURE add_phone(
    IN p_name VARCHAR(100),
    IN p_price DECIMAL(12,2),
    IN p_brand VARCHAR(50),
    IN p_stock INT
)
BEGIN
    INSERT INTO Products (name, price, brand, stock )
    VALUES (p_name, p_price, p_brand, p_stock);
END //

-- Cập nhật điện thoại
CREATE PROCEDURE update_phone(
    IN p_product_id INT,
    IN p_name VARCHAR(100),
    IN p_price DECIMAL(12,2),
    IN p_brand VARCHAR(50),
    IN p_stock INT
)
BEGIN
    UPDATE Products
    SET name = p_name, price = p_price, brand = p_brand, stock = p_stock
    WHERE productid = p_product_id;
END //

-- Xóa điện thoại
CREATE PROCEDURE delete_phone(
    IN p_product_id INT
)
BEGIN
    UPDATE products p
    SET p.status = 0
    WHERE p.productid = p_product_id;
END //

-- Tìm kiếm theo hãng
CREATE PROCEDURE find_phone_by_brand(
    IN p_brand VARCHAR(50)
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE brand LIKE CONCAT('%', p_brand, '%');
END //

-- Tìm kiếm theo khoảng giá
CREATE PROCEDURE find_phone_by_price_range(
    IN p_min_price DECIMAL(12,2),
    IN p_max_price DECIMAL(12,2)
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE price BETWEEN p_min_price AND p_max_price;
END //

-- Tìm kiếm theo tồn kho
CREATE PROCEDURE find_phone_by_stock(
    IN p_min_stock INT,
    IN p_max_stock INT
)
BEGIN
    SELECT productid, name, price, brand, stock, status
    FROM Products
    WHERE stock BETWEEN p_min_stock AND p_max_stock;
END //

DELIMITER ;


DELIMITER //
-- Lấy danh sách khách hàng
CREATE PROCEDURE get_customer_list()
BEGIN
    SELECT customer_id, name, email, phone, address FROM Customers
    WHERE status = TRUE
    ORDER BY name;
END //

-- Thêm khách hàng
CREATE PROCEDURE add_customer(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(15),
    IN p_address VARCHAR(255)
)
BEGIN
    INSERT INTO Customers (name, email, phone, address)
    VALUES (p_name, p_email, p_phone, p_address);
END //

-- Cập nhật khách hàng
CREATE PROCEDURE update_customer(
    IN p_customer_id INT,
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(15),
    IN p_address VARCHAR(255)
)
BEGIN
    UPDATE Customers
    SET name = p_name, email = p_email, phone = p_phone, address = p_address
    WHERE customer_id = p_customer_id;
END //

-- Xóa khách hàng
CREATE PROCEDURE delete_customer(
    IN p_customer_id INT
)
BEGIN
    UPDATE Customers
    SET status = 0
    WHERE customer_id = p_customer_id;
END //

DELIMITER ;


DELIMITER //
-- Lấy danh sách hóa đơn
CREATE PROCEDURE get_invoices()
BEGIN
    SELECT i.invoice_id, i.customer_id, i.invoice_date, i.total
    FROM Invoices i;
END //

-- Stored Procedure: Thêm hóa đơn
CREATE PROCEDURE add_invoice(
    IN p_customer_id INT,
    IN p_total_amount DECIMAL(12,2)
)
BEGIN
    INSERT INTO Invoices (customer_id, total)
    VALUES (p_customer_id , p_total_amount);
END //

-- Stored Procedure: Thêm chi tiết hóa đơn
CREATE PROCEDURE add_invoice_item(
    IN p_invoice_id INT,
    IN p_product_id INT,
    IN p_quantity INT,
    IN p_unit_price DECIMAL(12,2)
)
BEGIN
    INSERT INTO invoice_items (invoice_id, productid, quantity, unit_price)
    VALUES (p_invoice_id, p_product_id, p_quantity, p_unit_price);

    UPDATE products p

    SET  p.stock = p.stock - p_quantity
    WHERE p.productid = p_product_id;

END //

-- Stored Procedure: Tìm kiếm hóa đơn theo tên khách hàng
CREATE PROCEDURE search_invoices_by_customer_name(
    IN p_customer_name VARCHAR(100)
)
BEGIN
    SELECT i.invoice_id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total
    FROM Invoices i
             JOIN Customers c ON i.customer_id = c.customer_id
    WHERE c.name LIKE CONCAT('%', p_customer_name, '%');
END //

-- Stored Procedure: Tìm kiếm hóa đơn theo ngày
CREATE PROCEDURE search_invoices_by_date(
    IN p_date DATE
)
BEGIN
    SELECT i.invoice_id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total
    FROM Invoices i
             JOIN Customers c ON i.customer_id = c.customer_id
    WHERE DATE(i.invoice_date) = p_date ;
END //

DELIMITER ;