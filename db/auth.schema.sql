create table if not exists users
(
    id       bigint primary key generated by default as identity,
    login    varchar(50) unique not null,
    password varchar(50)        not null,
    fullname varchar(50)        not null
);

create table if not exists token
(
    id            bigint primary key generated by default as identity,
    authenticated boolean                 not null,
    uuid          varchar(36) unique      not null,
    user_id       bigint references users not null
);
