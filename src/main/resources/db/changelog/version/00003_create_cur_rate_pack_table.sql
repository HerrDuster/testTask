CREATE SEQUENCE public.cur_rate_pack_id_seq;

CREATE TABLE public.cur_rate_pack
(
    id              bigint    NOT NULL DEFAULT nextval('cur_rate_pack_id_seq'),
    outer_system_id bigint    NOT NULL,
    unload_time     timestamp NOT NULL,
    start_time      timestamp NOT NULL,
    CONSTRAINT cur_rate_pack_pk PRIMARY KEY (id)
);

ALTER TABLE public.cur_rate_pack
    ADD CONSTRAINT cur_rate_pack_fk FOREIGN KEY (outer_system_id) REFERENCES public.outer_system (id);


