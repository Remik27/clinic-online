package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.PatientEntity;

import java.util.Optional;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Integer> {
    Optional<PatientEntity> findByPesel(String pesel);
}
