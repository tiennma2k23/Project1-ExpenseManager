create table user_db
(
    id bigint auto_increment primary key,
    email    varchar(255) null,
    name     varchar(255) null,
    password varchar(255) null,
    constraint email
        unique (email)
);

