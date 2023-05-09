ALTER SEQUENCE certificate.gift_certificate_id_seq RESTART WITH 1;
ALTER SEQUENCE certificate.tag_id_seq RESTART WITH 1;

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
       (1, 5),
       (1, 6);
