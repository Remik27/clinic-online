package pl.zajavka.business.dao;

import pl.zajavka.domain.Doctor;

public interface DoctorDao {
    void saveAllTerms(Doctor doctor);

    Doctor saveDoctor(Doctor doctor);
}
