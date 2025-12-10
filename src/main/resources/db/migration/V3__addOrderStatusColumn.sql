ALTER TABLE "online-shop_schema"."order"
    ADD COLUMN order_status VARCHAR(20) NOT NULL DEFAULT 'NEW';