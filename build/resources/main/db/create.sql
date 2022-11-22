CREATE DATABASE myloans;
\c myloans;
CREATE TABLE loans (id SERIAL PRIMARY KEY, description VARCHAR, completed BOOLEAN, categoryid INTEGER);
CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR);
CREATE DATABASE myloans_test WITH TEMPLATE myloans;