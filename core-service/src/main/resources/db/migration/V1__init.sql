CREATE TABLE products (
    id bigserial  PRIMARY KEY,
    title         VARCHAR(255),
    price         numeric(9, 2) not null,
    created_at    timestamp default current_timestamp,
    updated_at    timestamp default current_timestamp
                      );

INSERT INTO products (title, price)
VALUES ('Milk', 150.00),
       ('Potatoes', 80.00),
       ('Eggs', 90.00),
       ('Pancake', 75.00),
       ('Paper', 150.00),
       ('Oil', 50.00),
       ('Salt', 10.00),
       ('Pineapple', 200.00),
       ('Watermelon', 175.00),
       ('Candy', 87.00);

create table orders
(
    id          bigserial primary key,
    username    varchar(255) not null,
    total_price numeric(9, 2) not null,
    address     varchar(255),
    phone       varchar(255),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

create table order_items
(
    id                bigserial primary key,
    product_id        bigint not null references products (id),
    order_id          bigint not null references orders (id),
    quantity          int    not null,
    price_per_product numeric(9, 2) not null,
    price             numeric(9, 2) not null,
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);

insert into orders (username, total_price, address, phone)
values ('user', 200.00, 'address', '12345');

insert into order_items (product_id, order_id, quantity, price_per_product, price)
values (1, 1, 2, 100.00, 200.00);
