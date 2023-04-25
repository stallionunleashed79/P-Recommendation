ALTER TABLE recommendation_template_section_field_mappings ADD MIN_PROPERTY_VALUE text;
ALTER TABLE recommendation_template_section_field_mappings ADD MAX_PROPERTY_VALUE text;
UPDATE recommendation_template_section_field_mappings SET MIN_PROPERTY_VALUE = '0' WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'valueEstimateFailureCost');
UPDATE recommendation_template_section_field_mappings SET MIN_PROPERTY_VALUE = '0' WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'valueEstimateRepairCost');
UPDATE recommendation_template_section_field_mappings SET MIN_PROPERTY_VALUE = '0' WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'smu');


