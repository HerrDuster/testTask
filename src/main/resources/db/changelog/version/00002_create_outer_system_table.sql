CREATE SEQUENCE public.outer_system_id_seq;

CREATE TABLE public.outer_system
(
    id             bigint       NOT NULL DEFAULT nextval('outer_system_id_seq'),
    code           varchar(10)  NOT NULL,
    outer_sys_name varchar(255) NOT NULL,
    link           varchar(255) not null,
    CONSTRAINT outer_system_pk PRIMARY KEY (id),
    CONSTRAINT outer_system_un UNIQUE (code)
);


