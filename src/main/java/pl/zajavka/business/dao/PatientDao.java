package pl.zajavka.business.dao;

import pl.zajavka.domain.Patient;

public interface PatientDao {
    Patient findPatient(Patient patient);

    Patient addPatient(Patient patient);

    boolean checkExistencePatient(Patient patient);

    Patient findPatientByPesel(String pesel);
}
