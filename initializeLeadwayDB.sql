DROP DATABASE IF EXISTS leadwaydb;
CREATE DATABASE leadwaydb;

-- connects to leadwaydb
\c 'leadwaydb';

DROP OWNED BY leadwayroot;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM leadwayroot;
REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM leadwayroot;
REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public FROM leadwayroot;

DROP USER IF EXISTS leadwayroot;
CREATE USER leadwayroot WITH PASSWORD 'leadwaypassword';

-- grant privilage to Vus manager

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO leadwayroot;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public to leadwayroot;
