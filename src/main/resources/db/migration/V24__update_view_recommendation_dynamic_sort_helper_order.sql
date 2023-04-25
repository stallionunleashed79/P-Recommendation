DROP VIEW recommendation_dynamic_sort_helper;
CREATE OR REPLACE VIEW recommendation_dynamic_sort_helper AS
SELECT *
FROM  crosstab(
  'SELECT recommendation_id, recommendation_number, is_dealer_recommendation, dealer_code, serial_number, title, site, asset_name, customer_ucid, created_by_catrecid, owner, field_name, value FROM (SELECT rcd.recommendation_id, recommendation_number, is_dealer_recommendation, dealer_code, serial_number, title, site, asset_name, customer_ucid, created_by_catrecid, owner, field_name, value FROM public.recommendation_common_data rcd
  LEFT JOIN public.recommendation_custom_data rcd2 on rcd.recommendation_id = rcd2.recommendation_id
  LEFT JOIN public.recommendation_fields rf on rf.field_id = rcd2.field_id ORDER BY recommendation_id) as sub_result'
,$$VALUES ('recommendationStatus'::text), ('recommendationPriority'), ('workOrderNumber')$$
) AS ct ("recommendation_id" int, "recommendation_number" text, "is_dealer_recommendation" boolean, "dealer_code" text, "serial_number" text, "title" text, "site" text, "asset_name" text, "customer_ucid" text, "created_by_catrecid" text, "owner" text, "recommendation_status" text, "recommendation_priority" text, "work_order_number" text)