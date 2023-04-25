CREATE TABLE IF NOT EXISTS asset_details
(
    asset_details_id SERIAL UNIQUE,
    serial_number VARCHAR(20) NOT NULL,
    make VARCHAR(20) NOT NULL,
    primary_customer_number VARCHAR(20) NOT NULL,
    primary_customer_name VARCHAR(50),
    dealer_code VARCHAR(20),
    dealer_name VARCHAR(50),
    site text,
    smu INTEGER NOT NULL,
    model VARCHAR(20),
    enabled BOOLEAN NOT NULL
);
