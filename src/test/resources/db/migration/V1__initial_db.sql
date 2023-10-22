USE shortening_url_test;
-- Table: url
DROP TABLE IF EXISTS url;
CREATE TABLE url
(
    id bigint NOT NULL,
    updated_at timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    original_url character varying(255) NOT NULL,
    short_url character varying(255) NOT NULL,
    CONSTRAINT url_pkey PRIMARY KEY (id)
);

-- Table: review
DROP TABLE IF EXISTS review;
CREATE TABLE review
(
    id bigint NOT NULL,
    updated_at timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    url_id bigint,
    ip character varying(255) NOT NULL,
    CONSTRAINT review_pkey PRIMARY KEY (id),
    CONSTRAINT fkbb5b8wrvisfrv02u93e1pbx5v FOREIGN KEY (url_id)
        REFERENCES url (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

