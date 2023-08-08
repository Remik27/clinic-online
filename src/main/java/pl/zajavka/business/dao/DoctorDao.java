package pl.zajavka.business.dao;

import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;

import java.util.List;

public interface DoctorDao {
    List<FreeTerm> saveAllTerms(Doctor doctor);

    Doctor saveDoctor(Doctor doctor);
}
