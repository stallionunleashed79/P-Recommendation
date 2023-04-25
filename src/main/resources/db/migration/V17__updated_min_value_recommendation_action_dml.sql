UPDATE recommendation_template_section_field_mappings SET MIN_LENGTH = null WHERE
field_id = (SELECT field_id FROM recommendation_fields where field_name = 'recommendationAction');
