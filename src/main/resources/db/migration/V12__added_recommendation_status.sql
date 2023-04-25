ALTER TABLE recommendation_collection_options ADD CONSTRAINT option_name_unique UNIQUE (option_name);

INSERT INTO recommendation_collection_options (option_name, option_value, collection_id) VALUES ('Completed', '2', 1);

UPDATE recommendation_collection_options SET option_value = '3' where option_name = 'Expired';
