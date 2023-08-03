CREATE TABLE patient
(
	id 		SERIAL 			NOT NULL,
	clinic_user_id INT 			NOT NULL,
	name 	VARCHAR(255) 	NOT NULL,
	surname	VARCHAR(255)	NOT NULL	UNIQUE,
	pesel	VARCHAR(12)		NOT NULL	UNIQUE,
	PRIMARY KEY(id),
	CONSTRAINT fk_clinic_user
		FOREIGN KEY(clinic_user_id)
			REFERENCES clinic_user (id)
);