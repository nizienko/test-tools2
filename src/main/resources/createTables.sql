CREATE TABLE execution
(
  id serial NOT NULL,
  data jsonb,
  execution_dt timestamp with time zone,
  last_change_dt timestamp with time zone,
  CONSTRAINT execution_id_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE execution
  OWNER TO postgres;