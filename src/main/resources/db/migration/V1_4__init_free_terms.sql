CREATE TABLE free_terms
(
	id			SERIAL 						NOT NULL,
	doctor_id	INT							NOT NULL,
	term		TIMESTAMP WITH TIME ZONE 	NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_doctor
		FOREIGN KEY(doctor_id)
			REFERENCES doctor(id)
);