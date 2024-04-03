drop table if exists professor_subject;
drop table if exists  professor;
drop table if exists  department;
drop table if exists  subject;

create table if not exists department (
    id serial primary key,
    name      varchar(100) not null unique
);

create table if not exists professor (
    id serial       primary key,
    name            varchar(30) not null,
    surname         varchar(30) not null,
    department_id   integer references department(id)
);

create table if not exists subject (
    id serial      primary key,
    name           varchar(30) not null,
    value_of_hours integer
);

create table professor_subject (
    professor_id integer not null references professor(id),
    subject_id   integer not null references subject(id),
    primary key (professor_id, subject_id)
);


