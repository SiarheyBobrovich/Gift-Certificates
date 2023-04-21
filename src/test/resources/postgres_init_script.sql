CREATE SCHEMA IF NOT EXISTS certificate;

CREATE SEQUENCE IF NOT EXISTS certificate.gift_certificate_seq
	START 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	INCREMENT BY 1
	CACHE 1;

CREATE SEQUENCE IF NOT EXISTS certificate.tag_seq
	START 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	INCREMENT BY 1
	CACHE 1;

CREATE TABLE IF NOT EXISTS certificate.gift_certificate (
	id              	BIGINT         			        PRIMARY KEY NOT NULL     DEFAULT nextval('certificate.gift_certificate_seq'),
	name        		VARCHAR(30)       		        NOT NULL,
	description		    TEXT	   			            NOT NULL,
	price			    DECIMAL				            NOT NULL,
	duration		    INTEGER				            NOT NULL,
	create_date		    TIMESTAMP WITHOUT TIME ZONE	    NOT NULL,
	last_update_date	TIMESTAMP WITHOUT TIME ZONE	    NOT NULL
);

CREATE TABLE IF NOT EXISTS certificate.tag (
	id              	BIGINT         			    PRIMARY KEY     	DEFAULT nextval('certificate.tag_seq'),
	name            	VARCHAR(20)  	   			NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS certificate.gc_tag (
	gc_id		        BIGINT          		NOT NULL,
	t_id            	BIGINT          		NOT NULL
);

ALTER TABLE certificate.gc_tag ADD CONSTRAINT gc_id_ON_gift_c_id_FK FOREIGN KEY (gc_id) REFERENCES certificate.gift_certificate (id);
ALTER TABLE certificate.gc_tag ADD CONSTRAINT t_id_ON_tag_id_FK FOREIGN KEY (t_id) REFERENCES certificate.tag (id);

CREATE OR REPLACE FUNCTION call(part VARCHAR)
    RETURNS TABLE (
            t_id BIGINT,
            t_name VARCHAR,
            gc_id BIGINT,
            gc_name VARCHAR,
            gc_description TEXT,
            gc_price DECIMAL,
            gc_duration INTEGER,
            gc_create_date TIMESTAMP WITHOUT TIME ZONE,
            gc_last_update_date TIMESTAMP WITHOUT TIME ZONE
) AS $$
    BEGIN
        RETURN QUERY SELECT
                t.id AS t_id,
                t.name AS t_name,
                gc.id AS gc_id,
                gc.name AS gc_name,
                gc.description AS gc_description,
                gc.price AS gc_price,
                gc.duration AS gc_duration,
                gc.create_date AS gc_create_date,
                gc.last_update_date AS gc_last_update_date
            FROM
                certificate.gc_tag gct
            JOIN
                certificate.gift_certificate gc ON gc.id = gct.gc_id
            JOIN
                certificate.tag t ON t.id = gct.t_id
            WHERE
               gc.name LIKE concat('%', part, '%') OR
               gc.description LIKE concat('%', part, '%');
    END $$
LANGUAGE 'plpgsql';

INSERT INTO certificate.gift_certificate (
    name,
    description,
    price,
    duration,
    create_date,
    last_update_date)
    VALUES
        ('first','first certificate','1.11',11,'2023-04-01 00:00:00',CURRENT_TIMESTAMP),
        ('second','second certificate','2.22',22,'2023-04-02 00:00:00',CURRENT_TIMESTAMP),
        ('third','third certificate','3.33',33,'2023-04-03 00:00:00',CURRENT_TIMESTAMP),
        ('fourth','fourth certificate','4.44',44,'2023-04-04 00:00:00',CURRENT_TIMESTAMP);

INSERT INTO certificate.tag (name)
    VALUES
        ('first tag'),
        ('second tag'),
        ('third tag'),
        ('fourth tag');

INSERT INTO certificate.gc_tag (gc_id, t_id)
    VALUES
        (1, 1),
        (1, 2),
        (2, 2),
        (2, 3),
        (3, 3);