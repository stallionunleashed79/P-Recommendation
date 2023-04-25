-- INITIAL DATA FOR GROUPS TABLE
INSERT INTO recommendation_groups (group_name) VALUES ('Status');
INSERT INTO recommendation_groups (group_name) VALUES ('Currency');
INSERT INTO recommendation_groups (group_name) VALUES ('Priority');

-- INITIAL DATA FOR OPTIONS TABLE
-- Group Id = 1 Means: Status of recommendation
-- Group Id = 2 Means: Currency of recommendation
-- Group Id = 3 Means: Priority of recommendation

--Status values
INSERT INTO options (option_name, option_value, group_id) VALUES ('Draft', '0', 1);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Sent', '1', 1);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Expired', '2', 1);

--Currency values
INSERT INTO options (option_name, option_value, group_id) VALUES ('Afghani', 'AFN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Euro', 'EUR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lek', 'ALL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Algerian Dinar', 'DZD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('US Dollar', 'USD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kwanza', 'AOA', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('East Caribbean Dollar', 'XCD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Argentine Peso', 'ARS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Armenian Dram', 'AMD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Aruban Florin', 'AWG', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Australian Dollar', 'AUD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Azerbaijan Manat', 'AZN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Bahamian Dollar', 'BSD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Bahraini Dinar', 'BHD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Taka', 'BDT', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Barbados Dollar', 'BBD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Belarusian Ruble', 'BYN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Belize Dollar', 'BZD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('CFA Franc BCEAO', 'XOF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Bermudian Dollar', 'BMD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Indian Rupee', 'INR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Ngultrum', 'BTN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Boliviano', 'BOB', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Mvdol', 'BOV', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Convertible Mark', 'BAM', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Pula', 'BWP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Norwegian Krone', 'NOK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Brazilian Real', 'BRL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Brunei Dollar', 'BND', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Bulgarian Lev', 'BGN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Burundi Franc', 'BIF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Cabo Verde Escudo', 'CVE', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Riel', 'KHR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('CFA Franc BEAC', 'XAF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Canadian Dollar', 'CAD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Cayman Islands Dollar', 'KYD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Chilean Peso', 'CLP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Unidad de Fomento', 'CLF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Yuan Renminbi', 'CNY', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Colombian Peso', 'COP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Unidad de Valor Real', 'COU', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Comorian Franc ', 'KMF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Congolese Franc', 'CDF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('New Zealand Dollar', 'NZD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Costa Rican Colon', 'CRC', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kuna', 'HRK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Cuban Peso', 'CUP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Peso Convertible', 'CUC', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Netherlands Antillean Guilder', 'ANG', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Czech Koruna', 'CZK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Danish Krone', 'DKK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Djibouti Franc', 'DJF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Dominican Peso', 'DOP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Egyptian Pound', 'EGP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('El Salvador Colon', 'SVC', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Nakfa', 'ERN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lilangeni', 'SZL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Ethiopian Birr', 'ETB', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Falkland Islands Pound', 'FKP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Fiji Dollar', 'FJD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('CFP Franc', 'XPF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Dalasi', 'GMD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lari', 'GEL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Ghana Cedi', 'GHS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Gibraltar Pound', 'GIP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Quetzal', 'GTQ', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Pound Sterling', 'GBP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Guinean Franc', 'GNF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Guyana Dollar', 'GYD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Gourde', 'HTG', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lempira', 'HNL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Hong Kong Dollar', 'HKD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Forint', 'HUF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Iceland Krona', 'ISK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Rupiah', 'IDR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('SDR (Special Drawing Right)', 'XDR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Iranian Rial', 'IRR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Iraqi Dinar', 'IQD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('New Israeli Sheqel', 'ILS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Jamaican Dollar', 'JMD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Yen', 'JPY', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Jordanian Dinar', 'JOD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Tenge', 'KZT', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kenyan Shilling', 'KES', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('North Korean Won', 'KPW', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Won', 'KRW', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kuwaiti Dinar', 'KWD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Som', 'KGS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lao Kip', 'LAK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Lebanese Pound', 'LBP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Loti', 'LSL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Rand', 'ZAR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Liberian Dollar', 'LRD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Libyan Dinar', 'LYD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Swiss Franc', 'CHF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Pataca', 'MOP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Malagasy Ariary', 'MGA', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Malawi Kwacha', 'MWK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Malaysian Ringgit', 'MYR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Rufiyaa', 'MVR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Ouguiya', 'MRU', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Mauritius Rupee', 'MUR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('ADB Unit of Account', 'XUA', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Mexican Peso', 'MXN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Mexican Unidad de Inversion (UDI)', 'MXV', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Moldovan Leu', 'MDL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Tugrik', 'MNT', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Moroccan Dirham', 'MAD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Mozambique Metical', 'MZN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kyat', 'MMK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Namibia Dollar', 'NAD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Nepalese Rupee', 'NPR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Cordoba Oro', 'NIO', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Naira', 'NGN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Denar', 'MKD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Rial Omani', 'OMR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Pakistan Rupee', 'PKR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Balboa', 'PAB', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Kina', 'PGK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Guarani', 'PYG', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Sol', 'PEN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Philippine Peso', 'PHP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Zloty', 'PLN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Qatari Rial', 'QAR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Romanian Leu', 'RON', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Russian Ruble', 'RUB', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Rwanda Franc', 'RWF', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Saint Helena Pound', 'SHP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Tala', 'WST', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Dobra', 'STN', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Saudi Riyal', 'SAR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Serbian Dinar', 'RSD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Seychelles Rupee', 'SCR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Leone', 'SLL', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Singapore Dollar', 'SGD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Sucre', 'XSU', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Solomon Islands Dollar', 'SBD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Somali Shilling', 'SOS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('South Sudanese Pound', 'SSP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Sri Lanka Rupee', 'LKR', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Sudanese Pound', 'SDG', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Surinam Dollar', 'SRD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Swedish Krona', 'SEK', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('WIR Euro', 'CHE', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('WIR Franc', 'CHW', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Syrian Pound', 'SYP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('New Taiwan Dollar', 'TWD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Somoni', 'TJS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Tanzanian Shilling', 'TZS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Baht', 'THB', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Pa’anga', 'TOP', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Trinidad and Tobago Dollar', 'TTD', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Tunisian Dinar', 'TND', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Turkish Lira', 'TRY', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Turkmenistan New Manat', 'TMT', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Uganda Shilling', 'UGX', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Hryvnia', 'UAH', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('UAE Dirham', 'AED', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Peso Uruguayo', 'UYU', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Uruguay Peso en Unidades Indexadas (UI)', 'UYI', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Unidad Previsional', 'UYW', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Uzbekistan Sum', 'UZS', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Vatu', 'VUV', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Bolívar Soberano', 'VES', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Dong', 'VND', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Yemeni Rial', 'YER', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Zambian Kwacha', 'ZMW', 2);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Zimbabwe Dollar', 'ZWL', 2);

--Priority values
INSERT INTO options (option_name, option_value, group_id) VALUES ('Immediate Attention', '1', 3);
INSERT INTO options (option_name, option_value, group_id) VALUES ('At Next Stop', '2', 3);
INSERT INTO options (option_name, option_value, group_id) VALUES ('At Next Service', '3', 3);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Equipment Backlog', '4', 3);
INSERT INTO options (option_name, option_value, group_id) VALUES ('Information/Monitor', '5', 3);

-- INITIAL DATA FOR RECOMMENDATION_FIELD_TYPES TABLE
INSERT INTO recommendation_field_types (field_type_name)
VALUES ('TEXT');
INSERT INTO recommendation_field_types (field_type_name)
VALUES ('PICK_LIST');
INSERT INTO recommendation_field_types (field_type_name)
VALUES ('IMAGE');

-- INITIAL DATA FOR RECOMMENDATION_DATA_TYPES TABLE
INSERT INTO recommendation_data_types (data_name) VALUES ('String');
INSERT INTO recommendation_data_types (data_name) VALUES ('Integer');
INSERT INTO recommendation_data_types (data_name) VALUES ('Double');
INSERT INTO recommendation_data_types (data_name) VALUES ('Boolean');

-- INITIAL DATA FOR RECOMMENDATION_FIELDS TABLE
--Assuming that data type is going to be used on backend side
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('recommendationAction', 1, 1, null);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('recommendationDetails', 1, 1, null);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('recommendationPriority', 1, 2, 3);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('recommendationStatus', 1, 2, 1);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('valueEstimateCurrency', 1, 1, 2);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('valueEstimateFailureCost', 1, 1, null);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('valueEstimateRepairCost', 1, 1, null);
INSERT INTO recommendation_fields (field_name, data_type_id, field_type_id, group_id)
VALUES ('valueEstimateRecommendationValue', 1, 1, null);

-- INITIAL DATA FOR RECOMMENDATION_TEMPLATE
INSERT INTO recommendation_templates (template_name)
VALUES ('Template 1 - Default');

INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 1, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 2, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 3, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 4, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 5, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 6, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 7, FALSE);
INSERT INTO recommendation_template_fields (recommendation_template_id, recommendation_field_id, required)
VALUES (1, 8, FALSE);
