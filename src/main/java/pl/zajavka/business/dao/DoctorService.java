package pl.zajavka.business.dao;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.domain.Doctor;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorDao doctorDao;

    @Transactional
    void saveTerms(Doctor doctor){
        doctorDao.saveAllTerms(doctor);
    }

}
