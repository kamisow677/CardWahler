--liquibase formatted sql
--changeset arnenthar:1

insert into "session" ( password, link) values ('haslo1', 'link jakis');
insert into "session" ( password, link) values ( 'haslo2', 'link jakis');
insert into round (task_name, is_current, session_id) values ('zadanie 1', true, null);
insert into round (task_name, is_current, session_id) values ('zadanie 2', false, null);