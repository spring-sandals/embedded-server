drop database if exists sandals_test;
drop database if exists sandals_development;
drop user initialdev;

create database sandals_development;
create database sandals_test;
create user initialdev with password 'initialdev';
grant all privileges on database sandals_development to initialdev;
grant all privileges on database sandals_test to initialdev;

\connect sandals_development
grant usage, create on schema public to initialdev;

\connect sandals_test
grant usage, create on schema public to initialdev;