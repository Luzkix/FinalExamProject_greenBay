DELETE
from products;
DELETE
FROM users;

INSERT INTO users (id, balance, email, password, username)
VALUES (1, 0, 'zdenek@seznam.cz', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 'zdenek');
INSERT INTO users (id, balance, email, password, username)
VALUES (2, 1000, 'petr@seznam.cz', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 'petr');

INSERT INTO products (id, description, enlisting_time, name, photo_url, purchase_price, sold, sold_price, sold_time,
                      starting_price, buyer_id, seller_id)
VALUES (1, 'blue car', '2021-04-29 21:31:10', 'car', 'http://localhost:8080', 500, false, null, null, 100, null, 1);
