DELETE
from products;
DELETE
FROM users;

INSERT INTO users (id, balance, email, password, username)
VALUES (1, 0, 'test@seznam.cz', '$2a$10$ER.Kwx0Lkj29mDTHdbPsyuuriuY.MfEsSCXig07TcNBvhem25iL3S', 'zdenek');