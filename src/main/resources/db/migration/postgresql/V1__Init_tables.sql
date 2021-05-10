CREATE TABLE users (
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password    VARCHAR(100)    NOT NULL,
    balance     INTEGER         DEFAULT 0
);

CREATE TABLE products (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    description     VARCHAR(100)    NOT NULL,
    photo_url       VARCHAR(100)    NOT NULL,
    starting_price  INTEGER         NOT NULL,
    purchase_price  INTEGER         NOT NULL,
    sold            BOOLEAN,
    sold_price      INTEGER,
    enlisting_time  VARCHAR(100),
    sold_time       VARCHAR(100),
    highest_bid_id  BIGINT,
    seller_id       BIGINT,
    buyer_id        BIGINT,
    FOREIGN KEY (seller_id) REFERENCES users (id),
    FOREIGN KEY (buyer_id)  REFERENCES users (id)
);

CREATE TABLE bids (
    id              SERIAL PRIMARY KEY,
    bid_price       INTEGER         NOT NULL,
    bid_time        VARCHAR(100)    NOT NULL,
    product_id      BIGINT          NOT NULL,
    bidder_id       BIGINT          NOT NULL,
    FOREIGN KEY (product_id)    REFERENCES products (id),
    FOREIGN KEY (bidder_id)     REFERENCES users (id)
);