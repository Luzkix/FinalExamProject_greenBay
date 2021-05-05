CREATE TABLE users
(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    username    VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password    VARCHAR(100)    NOT NULL,
    balance     INTEGER         DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE products
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
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
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES users (id),
    FOREIGN KEY (buyer_id)  REFERENCES users (id)
);

CREATE TABLE bids
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    bid_price       INTEGER         NOT NULL,
    bid_time        VARCHAR(100)    NOT NULL,
    product_id      BIGINT          NOT NULL,
    bidder_id       BIGINT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id)    REFERENCES products (id),
    FOREIGN KEY (bidder_id)     REFERENCES users (id)
);