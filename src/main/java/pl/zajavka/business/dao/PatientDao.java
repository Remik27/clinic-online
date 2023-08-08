package pl.zajavka.business.dao;

import pl.zajavka.domain.Patient;

public interface PatientDao {
    Patient findPatient(Patient patient);
}
