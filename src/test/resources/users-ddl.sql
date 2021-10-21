set schema 'stinky_tooters';

drop table if exists stinky_tooters.users;
drop sequence if exists stinky_tooters.seq_user;

create sequence stinky_tooters.seq_usr;
create table stinky_tooters.users (
    usr_id                 numeric(15) primary key default nextval('stinky_tooters.seq_usr'),
    usr_name               varchar(20) unique not null
);

-- In production, but H2 doesn't support
-- create index idx_usr_name on stinky_tooters.users (usr_name);

insert into users (usr_id, user_name) values ("ST Diffusor");