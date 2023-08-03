CREATE TABLE visit
(
	id			SERIAL						NOT NULL,
	doctor_id	INT							NOT NULL,
	patient_id	INT							NOT NULL,
	description	VARCHAR(255),
	term		TIMESTAMP WITH TIME ZONE	NOT NULL,
	status		VARCHAR(32)					NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_doctor
		FOREIGN KEY(doctor_id)
			REFERENCES doctor(id),
	CONSTRAINT fk_patient
		FOREIGN KEY(patient_id)
			REFERENCES patient(id)
);