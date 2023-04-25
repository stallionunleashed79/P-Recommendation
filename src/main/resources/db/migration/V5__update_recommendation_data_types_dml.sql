/**
   Creating a new data type called 'Currency' to refer to currency values in input
 */
INSERT INTO recommendation_data_types (data_name) VALUES ('Currency');
/**
   Updating the data_type_id columns for recommendation field records
   with ids of fields that store currency values as they are currency amounts
 */
UPDATE
  recommendation_fields
SET
  data_type_id = (
    SELECT
      data_type_id
    FROM
      recommendation_data_types
    where
      data_name = 'Currency'
  )
where
  field_id IN (
    SELECT
      field_id
    FROM
      recommendation_fields
    where
      FIELD_NAME in (
        'valueEstimateFailureCost', 'valueEstimateRepairCost',
        'valueEstimateRecommendationValue'
      )
  );
