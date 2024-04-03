create table if not exists department (
                                          d_id serial primary key,
                                          d_name      varchar(100) not null unique
    );

create table if not exists professor (
                                         p_id serial       primary key,
                                         p_name            varchar(100) not null,
    surname         varchar(30) not null,
    department_id   integer references department(d_id)
    );

create table if not exists subject (
                                       s_id serial      primary key,
                                       s_name           varchar(100) not null,
    value_of_hours integer
    );

create table professor_subject (
                                   professor_id integer not null references professor(p_id),
                                   subject_id   integer not null references subject(s_id),
                                   primary key (professor_id, subject_id)
);
