-- Remove minimumPropertyLength for title
UPDATE recommendation_template_section_field_mappings SET MIN_LENGTH = null WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'title');

-- Added new dataType "User"
INSERT INTO recommendation_payload_types values (6, 'User');

-- Update recommendationStatus dataType to "STRING"
UPDATE recommendation_fields SET PAYLOAD_TYPE_ID = 1 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationStatus');

-- Update recommendationPriority with default value "3 - At Next Service"
UPDATE recommendation_template_section_field_mappings SET DEFAULT_VALUE = '3 - At Next Service' WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationPriority');

-- Updated expirationTime to isRequired false
UPDATE recommendation_template_section_field_mappings SET IS_FIELD_REQUIRED = false WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationOwner');

-- Update recommendationOwner dataType to "USER"
UPDATE recommendation_fields SET PAYLOAD_TYPE_ID = 6 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationOwner');

-- Update position of recommendedAction to 11
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 11 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationAction');

-- Added new inputType "SECTION_FOOTER_LABEL_LEFT"
INSERT INTO recommendation_field_types values (10, 'section-footer-label-left', false);

-- Update creationTime inputType to "SECTION_FOOTER_LABEL_LEFT"
UPDATE recommendation_fields SET FIELD_TYPE_ID = 10 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'createdTime');

-- Update position of createdTime to 12
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 12 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'createdTime');

-- Updated createdBy dataType to "USER"
UPDATE recommendation_fields SET PAYLOAD_TYPE_ID = 6 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'createdBy');

-- Updated createdBy inputType to "SECTION_FOOTER_LABEL_LEFT"
UPDATE recommendation_fields SET FIELD_TYPE_ID = 10 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'createdBy');

-- Update position of createdBy to 13
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 13 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'createdBy');

-- Updated updatedTime inputType to "SECTION_FOOTER_LABEL_RIGHT"
UPDATE recommendation_fields SET FIELD_TYPE_ID = 9 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'updatedTime');

-- Update position of updatedTime to 14
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 14 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'updatedTime');

-- Updated updatedBy dataType to "USER"
UPDATE recommendation_fields SET PAYLOAD_TYPE_ID = 6 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'updatedBy');

-- Updated updatedBy inputType to "SECTION_FOOTER_LABEL_LEFT"
UPDATE recommendation_fields SET FIELD_TYPE_ID = 9 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'updatedBy');

-- Update position of updatedBy to 13
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 15 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'updatedBy');

-- remove minimumPropertyLength by assigning to null
UPDATE recommendation_template_section_field_mappings SET MIN_LENGTH = null WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationDetails');

-- Updated minimumPropertyLength for recommendationDetails to isRequired false
UPDATE recommendation_template_section_field_mappings SET IS_FIELD_REQUIRED = false WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationDetails');

-- Added new sectionContainerType "SECTION_FOOTER_LABEL_LEFT"
INSERT INTO recommendation_template_section_container_types values (8, 'Accordion-LinksAttachments');

-- Update position of updatedBy to 13
UPDATE recommendation_template_sections SET SECTION_CONTAINER_TYPE_ID = 8 WHERE section_name = 'Links & Attachments';

-- Added new sectionContainerType "SECTION_FOOTER_LABEL_LEFT"
INSERT INTO recommendation_template_section_container_types values (9, 'Accordion-Table-Exception');

-- Update position of updatedBy to 13
UPDATE recommendation_template_sections SET SECTION_CONTAINER_TYPE_ID = 9 WHERE section_name = 'Related Exceptions';

-- Added new sectionContainerType "SECTION_FOOTER_LABEL_LEFT"
INSERT INTO recommendation_template_section_container_types values (10, 'Accordion-Table-Event');

-- Update position of updatedBy to 13
UPDATE recommendation_template_sections SET SECTION_CONTAINER_TYPE_ID = 10 WHERE section_name = 'Related Events';

-- Update position of workOrderNumber to 10
UPDATE recommendation_template_section_field_mappings SET FIELD_POSITION_NUMBER = 10 WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'workOrderNumber');
