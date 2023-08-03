CREATE TABLE clinic_user
(
	id 			SERIAL 			NOT NULL,
	username 	VARCHAR(32) 	NOT NULL,
	password	VARCHAR(32)		NOT NULL,
	email		VARCHAR(32)		NOT NULL,
	active		BOOLEAN			NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE ROLE
(
	id		SERIAL		NOT NULL,
	role	VARCHAR(32)	NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE clinic_user_role
(
	clinic_user_id	INT NOT NULL,
	role_id			INT	NOT NULL,
	PRIMARY KEY(clinic_user_id, role_id)
);