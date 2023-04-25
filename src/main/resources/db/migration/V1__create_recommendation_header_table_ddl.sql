CREATE TABLE IF NOT EXISTS recommendation_templates
(
    template_id SERIAL UNIQUE,
    CONSTRAINT recommendation_template_pk PRIMARY KEY (template_id),
    template_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_headers
(
    recommendation_id SERIAL UNIQUE, -- System integration
    CONSTRAINT recommendation_header_pk PRIMARY KEY (recommendation_id),
    recommendation_number VARCHAR (50) UNIQUE NOT NULL, -- REC-989-344-123 - Show to end users
    title text,
    smu INTEGER NOT NULL,
    owner VARCHAR(50), --keep catrecid FK
    asset_name text NOT NULL,
    site text NOT NULL,
    expiration_date timestamp NOT NULL,
    created_date timestamp NOT NULL DEFAULT (CURRENT_TIMESTAMP at time zone 'UTC'),
    updated_date timestamp NOT NULL,
    created_by_catrecid VARCHAR (100) NOT NULL,
    updated_by_catrecid VARCHAR (100) NOT NULL,
    template_id INTEGER NOT NULL,
    serial_number VARCHAR (100) NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR (100) NOT NULL,
    dealer_code VARCHAR (100),
    dealer_name text,
    customer_ucid VARCHAR (100),
    customer_name text,
    attachment_size NUMERIC (3, 2), --it will represent MB size for attachments of recommendation
    CONSTRAINT template_id_fk FOREIGN KEY (template_id) REFERENCES Recommendation_Templates(template_id),
    is_dealer_recommendation BOOLEAN NOT NULL
);

CREATE INDEX idx_recommendation_number ON recommendation_headers (recommendation_number);
CREATE INDEX idx_recommendation_created_date ON recommendation_headers (created_date);
CREATE INDEX idx_asset_name ON recommendation_headers (asset_name);
CREATE INDEX idx_site ON recommendation_headers (site);
CREATE INDEX idx_serial_number ON recommendation_headers (serial_number);

CREATE TABLE IF NOT EXISTS recommendation_field_types(
    field_type_id SERIAL UNIQUE,
    CONSTRAINT field_type_id_pk PRIMARY KEY (field_type_id),
    field_type_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_data_types (
    data_type_id SERIAL UNIQUE,
    CONSTRAINT data_type_id_pk PRIMARY KEY (data_type_id),
    data_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_groups (
    group_id SERIAL UNIQUE,
    CONSTRAINT recommendation_group_id_pk PRIMARY KEY (group_id),
    group_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_fields(
    field_id SERIAL UNIQUE,
    CONSTRAINT recommendation_fields_pk PRIMARY KEY (field_id),
    field_name text NOT NULL,
    data_type_id INTEGER NOT NULL,
    field_type_id INTEGER NOT NULL,
    group_id INTEGER,
    CONSTRAINT field_type_id_fk FOREIGN KEY (field_type_id) REFERENCES recommendation_field_types(field_type_id),
    CONSTRAINT data_type_id_fk FOREIGN KEY (data_type_id) REFERENCES recommendation_data_types(data_type_id),
    CONSTRAINT field_group_id_fk FOREIGN KEY (group_id) REFERENCES recommendation_groups(group_id)
);

CREATE TABLE IF NOT EXISTS recommendation_details (
    recommendation_id INTEGER NOT NULL,
    field_id INTEGER NOT NULL,
    value text NOT NULL,
    CONSTRAINT recommendation_number_field_id_pk PRIMARY KEY (recommendation_id, field_id),
    CONSTRAINT recommendation_field_id_fk FOREIGN KEY (field_id) REFERENCES Recommendation_Fields(field_id)
);

CREATE INDEX idx_recommendation_id_recommendation_details ON recommendation_details (recommendation_id);
CREATE INDEX idx_value_recommendation_details ON recommendation_details (value);

CREATE TABLE IF NOT EXISTS recommendation_template_fields (
    recommendation_template_id INTEGER NOT NULL,
    recommendation_field_id INTEGER NOT NULL,
    required BOOLEAN NOT NULL,
    CONSTRAINT template_fields_pk PRIMARY KEY (recommendation_template_id, recommendation_field_id)
);

CREATE TABLE IF NOT EXISTS options (
    option_id SERIAL UNIQUE,
    CONSTRAINT option_id_pk PRIMARY KEY (option_id),
    option_name text NOT NULL,
    option_value text NOT NULL,
    group_id INTEGER NOT NULL,
    CONSTRAINT options_group_id_fk FOREIGN KEY (group_id) REFERENCES recommendation_groups(group_id)
);

CREATE TABLE IF NOT EXISTS recommendation_audits (
    id SERIAL UNIQUE,
    CONSTRAINT recommendation_audit_pk PRIMARY KEY (id),
    recommendation_id INTEGER NOT NULL,
    recommendation_field_id INTEGER NOT NULL,
    old_value text NOT NULL,
    new_value text NOT NULL,
    modify_by VARCHAR(100) NOT NULL,
    CONSTRAINT recommendation_id_fk FOREIGN KEY (recommendation_id) REFERENCES recommendation_headers(recommendation_id),
    CONSTRAINT recommendation_field_id_fk FOREIGN KEY (recommendation_field_id) REFERENCES recommendation_fields(field_id)
);

CREATE TABLE IF NOT EXISTS recommendation_users (
    catrecid VARCHAR(50) UNIQUE NOT NULL,
    CONSTRAINT catrecid_pk PRIMARY KEY (catrecid),
    first_name text NOT NULL,
    last_name text NOT NULL,
    cws_id VARCHAR (50) NOT NULL
);
