create table accounts (
    id serial primary key,
    name varchar(100) unique not null,
    value decimal not null default '0.0'
);