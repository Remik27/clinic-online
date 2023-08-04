package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.PatientDao;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDao {
}
