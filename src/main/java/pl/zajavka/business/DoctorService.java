package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorDao doctorDao;

    @Transactional
    public void saveTerms(Doctor doctor){
        doctorDao.saveAllTerms(doctor);
    }

    @Transactional
    public Doctor saveDoctor(Doctor doctor){
        return doctorDao.saveDoctor(doctor);
    }

}
