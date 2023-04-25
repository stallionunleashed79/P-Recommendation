ALTER TABLE recommendation_users DROP COLUMN first_name;
ALTER TABLE recommendation_users DROP COLUMN last_name;
ALTER TABLE recommendation_users ADD COLUMN first_name text;
ALTER TABLE recommendation_users ADD COLUMN last_name text;