insert into clinic_user (id, username, email, password, active) values (1, 'doctor1','doctor1@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (id, username, email, password, active) values (2, 'doctor2','doctor2@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (id, username, email, password, active) values (3, 'doctor3','doctor3@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into clinic_user (id, username, email, password, active) values (5, 'patient5','patient5@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (id, username, email, password, active) values (6, 'patient6','patient6@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into clinic_user (id, username, email, password, active) values (7, 'patient7','patient7@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);



insert into clinic_user (id, username, email, password, active) values (8, 'test_user', 'test_user@email.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

UPDATE doctor SET clinic_user_id = 1 WHERE pesel = '12345678912';
UPDATE doctor SET clinic_user_id = 2 WHERE pesel = '12345678913';
UPDATE doctor SET clinic_user_id = 3 WHERE pesel = '22345678913';

UPDATE patient SET clinic_user_id = 5 WHERE pesel = '12345678412';
UPDATE patient SET clinic_user_id = 6 WHERE pesel = '12345672913';
UPDATE patient SET clinic_user_id = 7 WHERE pesel = '22345600913';

insert into role (id, role) values (1, 'DOCTOR'), (2, 'PATIENT'), (3, 'REST_API');

insert into clinic_user_role (clinic_user_id, role_id) values (1, 1), (2, 1), (3, 1), (4, 1);
insert into clinic_user_role (clinic_user_id, role_id) values (5, 2), (6, 2), (7, 2);
insert into clinic_user_role (clinic_user_id, role_id) values (8, 1), (8, 2);
