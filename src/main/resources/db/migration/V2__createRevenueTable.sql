CREATE TABLE revenue
(
    id            UUID             NOT NULL,
    location_id   UUID,
    date          date             NOT NULL,
    sales_revenue DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_revenue PRIMARY KEY (id)
);
ALTER TABLE revenue
    ADD CONSTRAINT FK_REVENUE_ON_LOCATION FOREIGN KEY (location_id) REFERENCES "online-shop_schema".location (id);