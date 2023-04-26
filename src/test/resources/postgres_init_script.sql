CREATE SCHEMA IF NOT EXISTS certificate;

CREATE TABLE IF NOT EXISTS certificate.gift_certificate
(
    id               BIGSERIAL PRIMARY KEY       NOT NULL,
    name             VARCHAR(30)                 NOT NULL,
    description      TEXT                        NOT NULL,
    price            DECIMAL                     NOT NULL,
    duration         INTEGER                     NOT NULL,
    create_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS certificate.tag
(
    id   BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(20)           NOT NULL UNIQUE,
    CONSTRAINT id_name_uniq UNIQUE (id, name)
);

CREATE TABLE IF NOT EXISTS certificate.gc_tag
(
    gc_id BIGINT NOT NULL,
    t_id  BIGINT NOT NULL
);

ALTER TABLE certificate.gc_tag
    ADD CONSTRAINT gc_id_ON_gift_c_id_FK FOREIGN KEY (gc_id) REFERENCES certificate.gift_certificate (id);
ALTER TABLE certificate.gc_tag
    ADD CONSTRAINT t_id_ON_tag_id_FK FOREIGN KEY (t_id) REFERENCES certificate.tag (id) ON DELETE CASCADE;

INSERT INTO certificate.gift_certificate (name,
                                          description,
                                          price,
                                          duration,
                                          create_date,
                                          last_update_date)
VALUES ('first', 'one certificate', '1.11', 11, '2023-04-01 00:00:00', CURRENT_TIMESTAMP),
       ('second', 'two certificate', '2.22', 22, '2023-04-02 00:00:00', CURRENT_TIMESTAMP),
       ('third', 'three certificate', '3.33', 33, '2023-04-03 00:00:00', CURRENT_TIMESTAMP),
       ('fourth', 'four certificate', '4.44', 44, '2023-04-04 00:00:00', CURRENT_TIMESTAMP);

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
       (1, 6),
       (2, 6);
