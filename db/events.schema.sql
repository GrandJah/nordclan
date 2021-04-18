create table if not exists event
(
    id          bigint primary key generated by default as identity,
    description varchar(500) not null,
    endevent    timestamp    not null,
    startevent  timestamp    not null,
    title       varchar(36)  not null,
    creator_id  bigint       not null references users
);

create table if not exists event_users
(
    event_id   bigint references users not null,
    members_id bigint references event not null
);
