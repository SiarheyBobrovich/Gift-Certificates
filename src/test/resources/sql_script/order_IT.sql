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
       ('second', 'two', '2.22', 22, '2023-04-02 00:00:00', CURRENT_TIMESTAMP);

INSERT INTO certificate.users (first_name, last_name)
VALUES ('One', 'First'),
       ('Two', 'Second');

INSERT INTO certificate.order (gc_id, price, user_id, purchase)
VALUES (1, 1, 1, '2023-01-01 00:00:00'),
       (1, 1, 1, '2023-01-01 00:00:00'),
       (1, 1, 1, '2023-01-01 00:00:00'),
       (1, 1, 1, '2023-01-01 00:00:00'),
       (2, 10, 2, '2023-01-01 00:00:00'),
       (1, 1, 2, '2023-01-01 00:00:00');
