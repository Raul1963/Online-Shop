CREATE TABLE location
(
    id             UUID         NOT NULL,
    name           VARCHAR(255) NOT NULL,
    country        VARCHAR(255),
    city           VARCHAR(255),
    county         VARCHAR(255),
    street_address VARCHAR(255),
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE "order"
(
    id             UUID                        NOT NULL,
    user_id        UUID,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    country        VARCHAR(255),
    city           VARCHAR(255),
    county         VARCHAR(255),
    street_address VARCHAR(255),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_detail
(
    location_id UUID,
    quantity    INTEGER NOT NULL,
    order_id    UUID    NOT NULL,
    product_id  UUID    NOT NULL,
    CONSTRAINT pk_orderdetail PRIMARY KEY (order_id, product_id)
);

CREATE TABLE product
(
    id          UUID             NOT NULL,
    name        VARCHAR(255)     NOT NULL,
    description VARCHAR(255)     NOT NULL,
    price       DECIMAL          NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    category_id UUID,
    image_url   VARCHAR(255)     NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE product_category
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT pk_productcategory PRIMARY KEY (id)
);

CREATE TABLE stock
(
    quantity    INTEGER NOT NULL,
    product_id  UUID    NOT NULL,
    location_id UUID    NOT NULL,
    CONSTRAINT pk_stock PRIMARY KEY (product_id, location_id)
);

CREATE TABLE "user"
(
    id            UUID         NOT NULL,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    user_name     VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    role          SMALLINT     NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_emailaddress UNIQUE (email_address);

ALTER TABLE order_detail
    ADD CONSTRAINT FK_ORDERDETAIL_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

ALTER TABLE order_detail
    ADD CONSTRAINT FK_ORDERDETAIL_ON_ORDER FOREIGN KEY (order_id) REFERENCES "order" (id);

ALTER TABLE order_detail
    ADD CONSTRAINT FK_ORDERDETAIL_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES product_category (id);

ALTER TABLE stock
    ADD CONSTRAINT FK_STOCK_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

ALTER TABLE stock
    ADD CONSTRAINT FK_STOCK_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);