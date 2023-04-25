-- INITIAL DATA FOR RECOMMENDATION SECTION TYPE TABLE
INSERT INTO recommendation_template_section_container_types (section_container_type_name) VALUES ('Form');
INSERT INTO recommendation_template_section_container_types (section_container_type_name) VALUES ('Accordion');
INSERT INTO recommendation_template_section_container_types (section_container_type_name) VALUES ('Grid');
INSERT INTO recommendation_template_section_container_types (section_container_type_name) VALUES ('Table');

-- INITIAL DATA FOR RECOMMENDATION TEMPLATE SECTIONS TABLE
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Recommendation', 0, 1);
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Recommendation Details', 1, 1);
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Links & Attachments', 2, 1);
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Value Estimate', 3, 1);
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Related Exceptions', 4, 4);
INSERT INTO recommendation_template_sections (section_name, section_position_number, section_container_type_id)
VALUES ('Related Events', 5, 4);

INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 1);
INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 2);
INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 3);
INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 4);
INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 5);
INSERT INTO recommendation_template_section_mappings (template_id, template_section_id) VALUES (1, 6);

-- INSERTING ANOTHER ROW INTO RECOMMENDATION FIELD TYPES TABLE TO ACCOMMODATE DATE-PICKER TYPE
UPDATE recommendation_field_types SET field_type_name = 'text' where field_type_name = 'TEXT';
UPDATE recommendation_field_types SET field_type_name = 'textarea' where field_type_id = 2;
UPDATE recommendation_field_types SET field_type_name = 'dropdown' where field_type_name = 'IMAGE';
INSERT INTO recommendation_field_types (field_type_name) VALUES ('checkbox');
INSERT INTO recommendation_field_types (field_type_name) VALUES ('button');
INSERT INTO recommendation_field_types (field_type_name) VALUES ('lookup');
INSERT INTO recommendation_field_types (field_type_name) VALUES ('date');
INSERT INTO recommendation_field_types (field_type_name) VALUES ('section-header-pill-right');
INSERT INTO recommendation_field_types (field_type_name) VALUES ('section-footer-label-right');

-- CLEARING OUT ALL FIELDS FROM RECOMMENDATION FIELDS TABLE
DELETE FROM recommendation_fields;

-- REMOVING THE CURRENCY PAYLOAD TYPE FROM RECOMMENDATION PAYLOAD TYPES TABLE AS IT IS NO LONGER NEEDED
DELETE FROM recommendation_payload_types where LOWER(payload_name) = LOWER('Currency');

-- INSERTING A NEW DATA TYPE - TIMESTAMP INTO RECOMMENDATION PAYLOAD TYPES TABLE FOR EXPIRATION DATE
INSERT INTO recommendation_payload_types (payload_type_id, payload_name) VALUES (5, 'Timestamp');

-- INSERTING A NEW ROW INTO RECOMMENDATION_COLLECTIONS TABLE FOR OWNER GROUP
INSERT INTO recommendation_field_collections (collection_name) VALUES ('Owner');
INSERT INTO recommendation_collection_options (option_name, option_value, collection_id) VALUES
('url', 'https://services.cat.com/catDigital/usermanagement/v1/users/search?username={​​username}​​', 4);

-- INSERTING COMMON/STATIC FIELDS OF A TEMPLATE INTO RECOMMENDATION FIELDS TABLE
ALTER SEQUENCE recommendation_fields_field_id_seq RESTART WITH 1;
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('title', 1, 1, null, 'Title', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('smu', 3, 1, null, 'SMU', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationNumber', 1, 1, null, 'Recommendation Number', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationOwner', 1, 6, 4, 'Owner', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('expirationTime', 5, 7, null, 'Expiration Date', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('assetId', 1, 1, null, 'Asset ID', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('customer', 1, 1, null, 'Customer', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('site', 1, 1, null, 'Site', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('createdTime', 5, 9, null, 'Created on', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('createdBy', 1, 9, null, 'by', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('updatedTime', 5, 8, null, 'Last Modified on', true);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('updatedBy', 1, 9, null, 'by', true);

-- INSERTING DYNAMIC FIELDS OF TEMPLATE INTO RECOMMENDATION FIELDS TABLE
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationPriority', 1, 3, 3, 'Priority', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationAction', 1, 2, null, 'Recommended Action', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationDetails', 1, 2, null, 'Recommendation Details', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('recommendationStatus', 2, 8, 1, 'Status', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('valueEstimateCurrency', 1, 3, 2, 'Currency', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('valueEstimateRecommendationValue', 3, 1, null, 'Recommendation Value', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('valueEstimateFailureCost', 3, 1, null, 'Estimated Failure Cost', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('valueEstimateRepairCost', 3, 1, null, 'Repair Cost', false);
INSERT INTO recommendation_fields (field_name, payload_type_id, field_type_id, collection_id, display_name, is_common_header_field)
VALUES ('valueEstimateDescription', 1, 2, null, 'Description', false);

--INITIAL DATA FOR RECOMMENDATION MAPPING SECTION (SECTION 1) FIELDS FOR STATIC/COMMON - RECOMMENDATION HEADER SECTION
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            default_value, is_read_only) VALUES (1, 16, true, 0, 'Draft', true); -- RECOMMENDATION STATUS
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            min_length, max_length) VALUES (1, 1, true, 1, 10, 200); -- TITLE
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            default_value) VALUES (1, 13, true, 2, 'At Next Service'); -- RECOMMENDATION PRIORITY
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            default_value) VALUES (1, 2, true, 3, '0'); -- SMU
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            min_length, is_read_only) VALUES (1, 3, true, 4, 15, true); -- RECOMMENDATION NUMBER
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number)
VALUES (1, 4, true, 5); -- RECOMMENDATION OWNER
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number)
VALUES (1, 5, false, 6); -- EXPIRATION TIME
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 6, false, 7, true); -- ASSET ID
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 7, false, 8, true); -- CUSTOMER
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 8, false, 9, true); -- SITE
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            min_length, max_length) VALUES (1, 14, true, 10, 50, 5000); -- RECOMMENDATION ACTION
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 9, false, 11, true); -- CREATE TIME
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 10, false, 12, true); -- CREATED BY
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 11, false, 13, true); -- UPDATED TIME
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (1, 12, false, 14, true); -- UPDATED BY
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            min_length, max_length) VALUES (2, 15, true, 0, 50, 5000); -- RECOMMENDATION DETAILS


-- INSERTING THE TEMPLATE SECTION MAPPING RECORDS FOR DYNAMIC FIELDS
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number
) VALUES (4, 17, false, 0); -- VALUE ESTIMATE CURRENCY
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (4, 18, false, 3, true); --VALUE ESTIMATE RECOMMENDATION VALUE
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (4, 19, false, 1, false); -- VALUE ESTIMATE FAILURE COST
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (4, 20, false, 2, false); -- VALUE ESTIMATE REPAIR COST
INSERT INTO recommendation_template_section_field_mappings (template_section_id, field_id, is_field_required, field_position_number,
                                                            is_read_only) VALUES (4, 21, false, 4, false); -- VALUE ESTIMATE DESCRIPTION

-- UPDATE EXISTING OPTIONS
UPDATE recommendation_collection_options SET option_name= 'Published' where option_name='Sent';
UPDATE recommendation_collection_options SET option_name='1 - Immediate Attention' where option_name='Immediate Attention';
UPDATE recommendation_collection_options SET option_name='2 - At Next Stop' where option_name='At Next Stop';
UPDATE recommendation_collection_options SET option_name='3 - At Next Service' where option_name='At Next Service';
UPDATE recommendation_collection_options SET option_name='4 - Equipment Backlog' where option_name='Equipment Backlog';
UPDATE recommendation_collection_options SET option_name='5 - Information/Monitor' where option_name='Information/Monitor';
