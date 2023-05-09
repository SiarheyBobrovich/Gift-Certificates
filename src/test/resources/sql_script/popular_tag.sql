ALTER SEQUENCE certificate.gift_certificate_id_seq RESTART WITH 1;
ALTER SEQUENCE certificate.tag_id_seq RESTART WITH 1;
ALTER SEQUENCE certificate.users_id_seq RESTART WITH 1;
ALTER SEQUENCE certificate.order_id_seq RESTART WITH 1;

INSERT INTO certificate.gift_certificate (name,
                                          description,
                                          price,
                                          duration,
                                          create_date,
                                          last_update_date)
VALUES ('first', 'one', '1.11', 11, '2023-04-01 00:00:00', CURRENT_TIMESTAMP),
       ('second', 'two', '2.22', 22, '2023-04-02 00:00:00', CURRENT_TIMESTAMP),
       ('third', 'three', '3.33', 33, '2023-04-03 00:00:00', CURRENT_TIMESTAMP),
       ('fourth', 'four', '4.44', 44, '2023-04-04 00:00:00', CURRENT_TIMESTAMP);

INSERT INTO certificate.tag (name)
VALUES ('#1'),
       ('#2'),
       ('#3'),
       ('#4'),
       ('#5'),
       ('#6');

INSERT INTO certificate.gc_tag (gc_id, t_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (1, 3),
       (1, 5),
       (1, 6);

INSERT INTO certificate.users (first_name, last_name)
VALUES ('One', 'First'),
       ('Two', 'Second'),
       ('Three', 'Third');

-- 1=14, 2=18, 3=4        2 user must buy
-- cert_id = 1, 2, 4, 3
-- result tag 3
INSERT INTO certificate.order (gc_id, price, user_id, purchase)
VALUES (1, 1, 1, CURRENT_TIMESTAMP),
       (1, 1, 1, CURRENT_TIMESTAMP),
       (1, 1, 1, CURRENT_TIMESTAMP),
       (1, 1, 2, CURRENT_TIMESTAMP),
       (2, 2, 2, CURRENT_TIMESTAMP),
       (2, 2, 2, CURRENT_TIMESTAMP),
       (2, 2, 2, CURRENT_TIMESTAMP),
       (2, 2, 3, CURRENT_TIMESTAMP),
       (2, 2, 3, CURRENT_TIMESTAMP),
       (3, 3, 1, CURRENT_TIMESTAMP),
       (3, 3, 2, CURRENT_TIMESTAMP),
       (4, 4, 1, CURRENT_TIMESTAMP),
       (4, 4, 1, CURRENT_TIMESTAMP),
       (4, 4, 2, CURRENT_TIMESTAMP),
       (4, 4, 2, CURRENT_TIMESTAMP);
