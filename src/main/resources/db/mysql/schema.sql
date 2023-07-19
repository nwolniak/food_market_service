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
    created_date       datetime NULL,
    last_modified_date datetime NULL,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS orders_items
(
    quantity INT    NULL,
    order_id BIGINT NOT NULL,
    item_id  BIGINT NOT NULL,
    CONSTRAINT pk_orders_items PRIMARY KEY (order_id, item_id)
);

-- ALTER TABLE orders_items
--     ADD CONSTRAINT FK_ORDERS_ITEMS_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id);
--
-- ALTER TABLE orders_items
--     ADD CONSTRAINT FK_ORDERS_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (order_id);
--
-- ALTER TABLE items_quantity
--     ADD CONSTRAINT FK_ITEMS_QUANTITY_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id);