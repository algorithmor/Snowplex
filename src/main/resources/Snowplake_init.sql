-- CREATE WAREHOUSE
CREATE WAREHOUSE IF NOT EXISTS REPORTS 
WITH WAREHOUSE_SIZE = XSMALL
AUTO_SUSPEND = 60
AUTO_RESUME = TRUE
INITIALLY_SUSPENDED = TRUE
MAX_CONCURRENCY_LEVEL = 2;

-- CREATE DATABASE
CREATE DATABASE REPORTS DATA_RETENTION_TIME_IN_DAYS = 0;
CREATE SCHEMA IF NOT EXISTS SNOWPLEX DATA_RETENTION_TIME_IN_DAYS = 0;


-- CREATE USER FOR REPORTS
-- MAKE SURE ONLY READ ACCESS
CREATE ROLE READER;
grant imported privileges on database SNOWFLAKE_SAMPLE_DATA to role READER;
grant usage on database REPORTS to role READER;
grant usage on schema REPORTS.SNOWPLEX to role READER;
grant select on all tables in schema REPORTS.SNOWPLEX to role READER;
grant usage on warehouse REPORTS to role READER;
grant select on future tables in schema "REPORTS"."SNOWPLEX" to role READER;
CREATE USER snowplex_reader;
GRANT ROLE reader to user snowplex_reader;
Alter user snowplex_reader SET PASSWORD='Re@d0nly' DEFAULT_ROLE=READER;

-- TABLE REPORT CONFIG
CREATE OR REPLACE TABLE "REPORTS"."SNOWPLEX"."REPORT_CONFIG" (report_id int, report_name varchar (128), report_description varchar (512), report_query text, report_params varchar (256), recipients varchar (512), destination_enum varchar (10), report_format_enum varchar (8), csv_delimiter varchar (10), warehouse_size varchar(8), created_by varchar (64), created_on timestamp, updated_on timestamp );

INSERT INTO "REPORTS"."SNOWPLEX"."REPORT_CONFIG"(report_id, report_name, report_description, report_query, report_params, recipients, destination_enum, report_format_enum, csv_delimiter, warehouse_size, created_by, created_on, updated_on) 
VALUES (1, 'Test', 'Test Report', 'SELECT C_NATIONKEY,SUM(C_ACCTBAL) FROM "SNOWFLAKE_SAMPLE_DATA"."TPCH_SF10"."CUSTOMER" WHERE C_NATIONKEY IN (:c_nationkey_list) GROUP BY C_NATIONKEY;',
        '{"c_nationkey_list" : [1,3,5] }', 'algorithmor', 'EMAIL', 'CSV', '|','S_WH','algorithmor', '2019-07-25 04:55:44', '2019-08-12 04:55:44');


