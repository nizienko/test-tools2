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