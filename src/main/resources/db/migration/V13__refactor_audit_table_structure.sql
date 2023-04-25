ALTER TABLE recommendation_audits DROP CONSTRAINT recommendation_audit_pk;
ALTER TABLE recommendation_audits DROP COLUMN id;
ALTER TABLE recommendation_audits ADD COLUMN id INTEGER NOT NULL;
ALTER TABLE recommendation_audits ADD COLUMN modified_date timestamp NOT NULL;
ALTER TABLE recommendation_audits RENAME COLUMN modify_by TO modified_by_catrecid;
ALTER TABLE recommendation_audits ADD PRIMARY KEY (recommendation_id, recommendation_field_id, id);