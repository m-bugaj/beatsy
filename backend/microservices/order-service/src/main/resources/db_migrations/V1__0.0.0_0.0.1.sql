-- Tworzenie funkcji aktualizującej modified_at
CREATE OR REPLACE FUNCTION set_modified_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE orders_id_seq;

ALTER SEQUENCE orders_id_seq
    OWNER TO admin;

CREATE TABLE orders
(
    id                BIGINT    DEFAULT NEXTVAL('orders_id_seq'::regclass) PRIMARY KEY,
    order_number      VARCHAR(50) UNIQUE NOT NULL,
    buyer_hash        VARCHAR(255)       NOT NULL,
    currency          VARCHAR(10)        NOT NULL,
    total_amount      NUMERIC(12, 2)     NOT NULL,
    status            VARCHAR(30)        NOT NULL,
    payment_status    VARCHAR(30)        NOT NULL,
    payment_provider  VARCHAR(30),
    payment_reference VARCHAR(255),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE order_items_id_seq;

ALTER SEQUENCE order_items_id_seq
    OWNER TO admin;

CREATE TABLE order_items
(
    id                 BIGINT                  DEFAULT NEXTVAL('order_items_id_seq'::regclass) PRIMARY KEY,
    order_id           BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    content_offer_hash VARCHAR(255)   NOT NULL,
    seller_hash        VARCHAR(255)   NOT NULL,
    product_type       VARCHAR(50)    NOT NULL, -- LICENSE | SERVICE | etc
    product_id         UUID           NOT NULL,
    product_name       VARCHAR(255)   NOT NULL,
    quantity           INT            NOT NULL DEFAULT 1,
    unit_price         NUMERIC(12, 2) NOT NULL,
    total_price        NUMERIC(12, 2) NOT NULL,
    metadata           JSONB,
    created_at         TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    modified_at        TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE order_status_history_id_seq;

ALTER SEQUENCE order_status_history_id_seq
    OWNER TO admin;

CREATE TABLE order_status_history
(
    id          BIGINT    DEFAULT NEXTVAL('order_status_history_id_seq'::regclass) PRIMARY KEY,
    order_id    BIGINT      NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    old_status  VARCHAR(30),
    new_status  VARCHAR(30) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE carts_id_seq;

ALTER SEQUENCE carts_id_seq
    OWNER TO admin;

CREATE TABLE carts
(
    id          BIGINT    DEFAULT NEXTVAL('carts_id_seq'::regclass) PRIMARY KEY,
    buyer_id    UUID        NOT NULL,
    currency    VARCHAR(10) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE cart_items_id_seq;

ALTER SEQUENCE cart_items_id_seq
    OWNER TO admin;

CREATE TABLE cart_items
(
    id                 BIGINT                DEFAULT NEXTVAL('cart_items_id_seq'::regclass) PRIMARY KEY,
    cart_id            BIGINT       NOT NULL REFERENCES carts (id) ON DELETE CASCADE,
    content_offer_hash VARCHAR(255) NOT NULL,
    quantity           INT          NOT NULL DEFAULT 1,
    created_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    modified_at        TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

-- INDEXY DO DODANIA POZNIEJ I ZASTANOWIENIA
-- CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
-- CREATE INDEX idx_orders_status ON orders(status);
-- CREATE INDEX idx_order_items_order_id ON order_items(order_id);
-- CREATE INDEX idx_order_payments_order_id ON order_payments(order_id);


CREATE TRIGGER trigger_orders_modified_at
    BEFORE UPDATE
    ON orders
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_order_items_modified_at
    BEFORE UPDATE
    ON order_items
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_order_status_history_modified_at
    BEFORE UPDATE
    ON order_status_history
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_carts_modified_at
    BEFORE UPDATE
    ON carts
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();

CREATE TRIGGER trigger_cart_items_modified_at
    BEFORE UPDATE
    ON cart_items
    FOR EACH ROW
EXECUTE FUNCTION set_modified_at();