CREATE EXTENSION IF NOT EXISTS tablefunc;

CREATE OR REPLACE VIEW recommendation_dynamic_sort_helper
AS SELECT *
FROM  crosstab(
	'SELECT recommendation_number, field_name, value FROM (SELECT * FROM public.recommendation_common_data rcd
	LEFT JOIN public.recommendation_custom_data rcd2 on rcd.recommendation_id = rcd2.recommendation_id
	LEFT JOIN public.recommendation_fields rf on rf.field_id = rcd2.field_id) as sub_result'
	,$$VALUES ('recommendationStatus'::text), ('recommendationPriority'), ('workOrderNumber')$$
) AS ct ("recommendation_number" text, "recommendation_status" int, "recommendation_priority" int, "work_order_number" text)