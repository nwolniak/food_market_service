CREATE TABLE IF NOT EXISTS products
(
    product_id         BIGINT       NOT NULL,
    name               VARCHAR(255) NULL,
    category           VARCHAR(255) NULL,
    unit_type          VARCHAR(255) NULL,
    unit_price         DOUBLE       NULL,
    `description`      VARCHAR(255) NULL,
    created_date       datetime     NULL,
    last_modified_date datetime     NULL,
    CONSTRAINT pk_products PRIMARY KEY (product_id)
);

CREATE TABLE IF NOT EXISTS product_counts
(
    product_id         BIGINT   NOT NULL,
    quantity_in_stock  INT      NULL,
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    CONSTRAINT pk_product_counts PRIMARY KEY (product_id)
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id           BIGINT   NOT NULL,
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS orders_products
(
    quantity   INT    NULL,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT pk_orders_products PRIMARY KEY (order_id, product_id)
);

# ALTER TABLE orders_products
#     ADD CONSTRAINT FK_ORDERS_PRODUCTS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (order_id);
#
# ALTER TABLE orders_products
#     ADD CONSTRAINT FK_ORDERS_PRODUCTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (product_id);
#
# ALTER TABLE product_counts
#     ADD CONSTRAINT FK_PRODUCT_COUNTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (product_id);