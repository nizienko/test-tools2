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

CREATE TABLE parameters
(
  id serial NOT NULL,
  parameter text,
  description text,
  CONSTRAINT parameters_pk PRIMARY KEY (id),
  CONSTRAINT parameter_u UNIQUE (parameter)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parameters
  OWNER TO postgres;

CREATE TABLE testconfiguration
(
id serial NOT NULL,
tk text,
description text,
CONSTRAINT testconf_pk PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE testconfiguration
OWNER TO postgres;

CREATE TABLE testvalues
(
  tk_id integer NOT NULL,
  parameter_id integer NOT NULL,
  value text,
  CONSTRAINT testvalues_pk PRIMARY KEY (tk_id, parameter_id),
  CONSTRAINT parameter_id_fk FOREIGN KEY (parameter_id)
      REFERENCES parameters (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tk_id_fk FOREIGN KEY (tk_id)
      REFERENCES testconfiguration (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE testvalues
  OWNER TO postgres;


CREATE INDEX execution_project_index ON
      execution ((data->>'project'));

CREATE INDEX execution_version_index ON
      execution ((data->>'version'));

CREATE INDEX execution_build_index ON
      execution ((data->>'build'));

CREATE INDEX execution_execution_index ON
      execution ((data->>'execution'));

CREATE INDEX execution_issue_index ON
      execution ((data->>'issue'));

CREATE INDEX execution_status_index ON
      execution ((data->>'status'));

CREATE INDEX execution_status_index ON
      execution ((data->>'reason'));

CREATE INDEX acceptance_index ON
      execution ((data->>'acceptance'));

-- Sequence: service_id_seq

-- DROP SEQUENCE service_id_seq;

CREATE SEQUENCE service_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 13
  CACHE 1;
ALTER TABLE service_id_seq
  OWNER TO postgres;


  -- Sequence: testand_id_seq

-- DROP SEQUENCE testand_id_seq;

CREATE SEQUENCE testand_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 21
  CACHE 1;
ALTER TABLE testand_id_seq
  OWNER TO postgres;



  -- Table: resources

-- DROP TABLE resources;

CREATE TABLE resources
(
  id integer NOT NULL DEFAULT nextval('testand_id_seq'::regclass),
  data jsonb,
  type text,
  CONSTRAINT resources_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE resources
  OWNER TO postgres;




  -- Table: services

-- DROP TABLE services;

CREATE TABLE services
(
  id integer NOT NULL DEFAULT nextval('service_id_seq'::regclass),
  data jsonb,
  CONSTRAINT services_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE services
  OWNER TO postgres;

  