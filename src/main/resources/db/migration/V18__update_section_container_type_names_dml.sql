-- Insert new rows into section container types table to represent new types
INSERT INTO recommendation_template_section_container_types values (5, 'Fragment-Form-3');
INSERT INTO recommendation_template_section_container_types values (6, 'Accordion-Form-1');
INSERT INTO recommendation_template_section_container_types values (7, 'Accordion-Form-3');

--Update the template sections for Recommendation, Recommendation Details and Value Estimate
UPDATE recommendation_template_sections SET section_container_type_id = 5 where
section_name = 'Recommendation';
UPDATE recommendation_template_sections SET section_container_type_id = 6 where
section_name = 'Recommendation Details';
UPDATE recommendation_template_sections SET section_container_type_id = 7 where
section_name = 'Value Estimate';

-- Update the maxLength property for SMU field in the mappings table
UPDATE recommendation_template_section_field_mappings SET max_length = 20 where
field_id = 2;

-- Insert a new mock assert record for dealer code - 'TD00'
INSERT INTO asset_details (serial_number, make, primary_customer_number, primary_customer_name,
                           dealer_code, dealer_name, site, smu, model, enabled)
VALUES ('ERM00238', 'CAT', '2969484566', 'Glencore Queensland Limited', 'TD00','Test Dealer',
        'Caterpillar Proving Grounds',1200,'795F AC', true);