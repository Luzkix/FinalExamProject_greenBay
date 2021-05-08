DELETE
from bids;
DELETE
from products;
DELETE
FROM users;

/* Users */
INSERT INTO users (id, balance, email, password, username)
VALUES (1, 0, 'zdenek@seznam.cz', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 'zdenek');
INSERT INTO users (id, balance, email, password, username)
VALUES (2, 1000, 'petr@seznam.cz', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 'petr');

/*Zdenek´s products (4products)*/
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (1, 'blue car', '2021-05-01T17:39:55', 'car1', 'http://localhost:8080', 500, false, null, null, 100, null, 1);
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (2, 'blue car', '2021-05-01T18:39:55', 'car2', 'http://localhost:8080', 500, false, null, null, 100, null, 1);
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (3, 'blue car', '2021-05-01T19:39:55', 'car3', 'http://localhost:8080', 500, false, null, null, 100, null, 1);
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (4, 'blue car', '2021-05-01T20:39:55', 'car4', 'http://localhost:8080', 500, false, null, null, 100, null, 1);

/*Peter´s products (3products)*/
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (5, 'blue bike', '2021-05-01T21:39:55', 'bike1', 'http://localhost:8080', 100, false, null, null, 50, null, 2);
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (6, 'blue bike', '2021-05-01T22:39:55', 'bike2', 'http://localhost:8080', 100, false, null, null, 50, null, 2);
INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (7, 'sold bike', '2021-05-01T23:39:55', 'bike3', 'http://localhost:8080', 100, true, 100, '2021-06-01T08:00:00', 50, 1, 2);

/* Bids */
INSERT INTO bids (id, bid_price, bid_time, bidder_id, product_id)
VALUES (1, 200, '2021-05-02T23:39:55', 1, 6);

/* setting the bid ID to the product after the bid is created */
UPDATE products SET highest_bid_id = 1 WHERE products.id = 6;