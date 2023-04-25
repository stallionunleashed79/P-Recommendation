-- INSERTING DYNAMIC FIELDS OF TEMPLATE INTO RECOMMENDATION FIELDS TABLE
INSERT INTO recommendation_fields
(
    field_name,
    payload_type_id,
    field_type_id,
    collection_id,
    display_name,
    is_common_header_field
)
VALUES
(
    'workOrderNumber',
    (select payload_type_id from recommendation_payload_types where payload_name = 'String'),
    (select field_type_id from recommendation_field_types where field_type_name = 'text'),
    null,
    'Work Order Number',
    false
);

INSERT INTO recommendation_template_section_field_mappings
(
    template_section_id,
    field_id,
    is_field_required,
    field_position_number,
    default_value,
    is_read_only,
    max_length
)
VALUES
(
    (SELECT template_section_id FROM recommendation_template_sections WHERE section_name = 'Recommendation'),
    (SELECT field_id FROM recommendation_fields WHERE field_name = 'workOrderNumber'),
    false,
    11,
    '',
    false,
    225
);