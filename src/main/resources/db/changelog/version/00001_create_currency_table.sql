CREATE SEQUENCE public.currency_id_seq;

CREATE TABLE public.currency
(
    id bigint NOT NULL DEFAULT nextval('currency_id_seq'),
    code varchar(3) NOT NULL,
    name varchar(255) NOT NULL,
    CONSTRAINT currency_pk PRIMARY KEY (id),
    CONSTRAINT currency_un UNIQUE (code)
);
