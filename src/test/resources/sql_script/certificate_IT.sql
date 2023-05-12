ALTER SEQUENCE certificate.gift_certificate_id_seq RESTART WITH 1;
ALTER SEQUENCE certificate.tag_id_seq RESTART WITH 1;

INSERT INTO certificate.gift_certificate (name,
                                          description,
                                          price,
                                          duration,
                                          create_date,
                                          last_update_date)
VALUES ('Certificate', 'description', '1', 1, '2000-01-01T00:00:00', '2000-01-01T00:00:00'),
       ('Certificate2', 'description2', '2', 2, '1970-01-01 00:00:00', '1970-01-01 00:00:00');

INSERT INTO certificate.tag (name)
VALUES ('One'),
       ('Two'),
       ('Three');

INSERT INTO certificate.gc_tag (gc_id, t_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3);
