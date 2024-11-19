CREATE SEQUENCE public.cur_rate_id_seq;

CREATE TABLE public.cur_rate
(
    id bigint NOT NULL DEFAULT nextval('cur_rate_id_seq'),
    from_cur_id bigint    NOT NULL,
    to_cur_id bigint NOT NULL,
    rate numeric NOT NULL,
    cur_rate_pack_id bigint NOT NULL,
    CONSTRAINT cur_rate_pk PRIMARY KEY (id)
);

ALTER TABLE public.cur_rate
    ADD CONSTRAINT from_cur_id_fk FOREIGN KEY (from_cur_id) REFERENCES public.currency (id);
ALTER TABLE public.cur_rate
    ADD CONSTRAINT to_cur_id_fk FOREIGN KEY (to_cur_id) REFERENCES public.currency (id);
ALTER TABLE public.cur_rate
    ADD CONSTRAINT cur_rate_pack_id_fk FOREIGN KEY (cur_rate_pack_id) REFERENCES public.cur_rate_pack (id);


