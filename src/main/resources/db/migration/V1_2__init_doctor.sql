CREATE TABLE doctor
(
	id 				SERIAL 			NOT NULL,
	clinic_user_id 	INT,
	name 			VARCHAR(255) 	NOT NULL,
	surname			VARCHAR(255) 	NOT NULL,
	pesel 			VARCHAR(12) 	NOT NULL UNIQUE,
	specialization 	VARCHAR(255) 	NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_clinic_user
		FOREIGN KEY(clinic_user_id)
			REFERENCES clinic_user (id)
);


