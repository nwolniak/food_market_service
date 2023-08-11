CREATE TABLE IF NOT EXISTS items
(
    item_id            BIGINT       NOT NULL,
    name               VARCHAR(255) NULL,
    category           VARCHAR(255) NULL,
    unit_type          VARCHAR(255) NULL,
    unit_price         DOUBLE       NULL,
    `description`      VARCHAR(255) NULL,
    created_date       datetime     NULL,
    last_modified_date datetime     NULL,
    CONSTRAINT pk_items PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS items_quantity
(
    item_id            BIGINT   NOT NULL,
    quantity_in_stock  INT      NULL,
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    CONSTRAINT pk_items_quantity PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id           BIGINT   NOT NULL,
    is_paid            BIT(1)   NOT NULL,
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    user_id            BIGINT   NULL,
    payment_id         BIGINT   NULL,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS orders_items
(
    quantity INT    NULL,
    order_id BIGINT NOT NULL,
    item_id  BIGINT NOT NULL,
    CONSTRAINT pk_orders_items PRIMARY KEY (order_id, item_id)
);

CREATE TABLE IF NOT EXISTS carts
(
    cart_id            BIGINT   NOT NULL,
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    user_id            BIGINT   NULL,
    CONSTRAINT pk_carts PRIMARY KEY (cart_id)
);

CREATE TABLE IF NOT EXISTS carts_items
(
    quantity INT    NULL,
    cart_id  BIGINT NOT NULL,
    item_id  BIGINT NOT NULL,
    CONSTRAINT pk_carts_items PRIMARY KEY (cart_id, item_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id            BIGINT       NOT NULL,
    name               VARCHAR(255) NOT NULL,
    username           VARCHAR(255) NULL,
    password           VARCHAR(255) NULL,
    email              VARCHAR(255) NULL,
    created_date       datetime     NULL,
    last_modified_date datetime     NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uc_users_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS payments
(
    payment_id   BIGINT   NOT NULL,
    amount       DOUBLE   NOT NULL,
    payment_date datetime NULL,
    user_id      BIGINT   NULL,
    order_id     BIGINT   NULL,
    CONSTRAINT pk_payments PRIMARY KEY (payment_id)
);

# ALTER TABLE carts
#     ADD CONSTRAINT FK_CARTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

# ALTER TABLE carts_items
#     ADD CONSTRAINT FK_CARTS_ITEMS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (cart_id);
#
# ALTER TABLE carts_items
#     ADD CONSTRAINT FK_CARTS_ITEMS_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id);

-- ALTER TABLE orders_items
--     ADD CONSTRAINT FK_ORDERS_ITEMS_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id);
--
-- ALTER TABLE orders_items
--     ADD CONSTRAINT FK_ORDERS_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (order_id);
--
-- ALTER TABLE items_quantity
--     ADD CONSTRAINT FK_ITEMS_QUANTITY_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id);