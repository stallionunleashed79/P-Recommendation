ALTER TABLE recommendation_headers RENAME TO recommendation_common_data;
ALTER TABLE recommendation_details RENAME TO recommendation_custom_data;
ALTER TABLE recommendation_data_types RENAME TO recommendation_payload_types;
ALTER TABLE recommendation_payload_types RENAME COLUMN data_type_id TO payload_type_id;
ALTER TABLE recommendation_payload_types RENAME COLUMN data_name TO payload_name;
ALTER TABLE recommendation_fields ADD COLUMN display_name text NOT NULL DEFAULT '';
ALTER TABLE recommendation_fields ADD COLUMN is_common_header_field BOOLEAN NOT NULL default false;
ALTER TABLE recommendation_fields RENAME COLUMN data_type_id TO payload_type_id;
ALTER TABLE recommendation_fields RENAME CONSTRAINT data_type_id_fk TO payload_type_id_fk;
ALTER TABLE recommendation_field_types ADD COLUMN is_system_generated BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE recommendation_templates ADD COLUMN description text NOT NULL DEFAULT '';

CREATE TABLE IF NOT EXISTS recommendation_field_types(
     field_type_id SERIAL UNIQUE,
     CONSTRAINT field_type_id_pk PRIMARY KEY (field_type_id),
     field_type_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_template_section_container_types
(
    section_container_type_id SERIAL UNIQUE,
    CONSTRAINT section_container_type_id_pk PRIMARY KEY (section_container_type_id),
    section_container_type_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS recommendation_template_sections
(
    template_section_id SERIAL UNIQUE,
    section_name text NOT NULL,
    section_position_number INTEGER NOT NULL,
    section_container_type_id INTEGER NOT NULL,
    CONSTRAINT section_container_type_id_fk FOREIGN KEY (section_container_type_id) REFERENCES recommendation_template_section_container_types(section_container_type_id)
);

CREATE TABLE IF NOT EXISTS recommendation_template_section_mappings
(
    template_section_id INTEGER NOT NULL,
    template_id INTEGER NOT NULL,
    CONSTRAINT recommendation_template_section_pk PRIMARY KEY (template_section_id, template_id)
);

CREATE TABLE IF NOT EXISTS recommendation_template_section_field_mappings
(
    template_section_id INTEGER NOT NULL,
    field_id INTEGER NOT NULL,
    is_field_required BOOLEAN NOT NULL,
    is_read_only BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT recommendation_template_section_field_mapping_pk PRIMARY KEY (template_section_id, field_id),
    field_position_number INTEGER NOT NULL,
    default_value text,
    min_length INTEGER,
    max_length INTEGER
);

ALTER TABLE recommendation_groups RENAME TO recommendation_field_collections;
ALTER TABLE recommendation_field_collections RENAME COLUMN group_id TO collection_id;
ALTER TABLE recommendation_field_collections RENAME COLUMN group_name TO collection_name;
ALTER TABLE recommendation_field_collections RENAME CONSTRAINT recommendation_group_id_pk TO recommendation_collection_id_pk;
ALTER TABLE options RENAME TO recommendation_collection_options;
ALTER TABLE recommendation_collection_options RENAME COLUMN group_id to collection_id;
ALTER TABLE recommendation_collection_options RENAME CONSTRAINT options_group_id_fk to collection_options_collection_id_fk;
ALTER TABLE recommendation_fields RENAME group_id TO collection_id;
ALTER TABLE recommendation_fields RENAME CONSTRAINT field_group_id_fk to field_collection_id_fk;

