ALTER SEQUENCE certificate.tag_id_seq RESTART WITH 1;
INSERT INTO certificate.tag (name)
VALUES ('One'),
       ('Two');
