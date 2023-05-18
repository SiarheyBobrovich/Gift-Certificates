CREATE DATABASE clevertec;

\c clevertec

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
    name VARCHAR(20)           NOT NULL UNIQUE
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

CREATE TABLE certificate.users
(
    id         BIGSERIAL   NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE certificate.order
(
    id       BIGSERIAL                   NOT NULL,
    gc_id    BIGINT                      NOT NULL,
    price    DECIMAL                     NOT NULL,
    user_id  BIGINT                      NOT NULL,
    purchase TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE certificate."order"
    ADD CONSTRAINT FK_ORDERS_ON_GC FOREIGN KEY (gc_id) REFERENCES certificate.gift_certificate (id);

ALTER TABLE certificate."order"
    ADD CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES certificate.users (id);
