CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password    VARCHAR(100)    NOT NULL,
    balance     INTEGER         DEFAULT 0
);

CREATE TABLE products (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    description     VARCHAR(300)    NOT NULL,
    photo_url       VARCHAR(400)    NOT NULL,
    starting_price  INTEGER         NOT NULL,
    purchase_price  INTEGER         NOT NULL,
    sold            BOOLEAN,
    sold_price      INTEGER,
    enlisting_time  TIMESTAMP,
    sold_time       TIMESTAMP,
    highest_bid_id  BIGINT,
    seller_id       BIGINT,
    buyer_id        BIGINT,
    FOREIGN KEY (seller_id) REFERENCES users (id),
    FOREIGN KEY (buyer_id)  REFERENCES users (id)
);

CREATE TABLE bids (
    id              BIGSERIAL PRIMARY KEY,
    bid_price       INTEGER         NOT NULL,
    bid_time        TIMESTAMP       NOT NULL,
    product_id      BIGINT          NOT NULL,
    bidder_id       BIGINT          NOT NULL,
    FOREIGN KEY (product_id)    REFERENCES products (id),
    FOREIGN KEY (bidder_id)     REFERENCES users (id)
);