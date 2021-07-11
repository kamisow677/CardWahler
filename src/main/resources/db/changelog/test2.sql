--liquibase formatted sql
--changeset arnenthar:2


UPDATE round
SET session_id =  (
    SELECT id FROM "session"
    WHERE password = 'haslo1'
)
WHERE task_name = 'zadanie 1';

UPDATE round
SET session_id =  (
    SELECT id FROM "session"
    WHERE password = 'haslo1'
)
WHERE task_name = 'zadanie 2';
