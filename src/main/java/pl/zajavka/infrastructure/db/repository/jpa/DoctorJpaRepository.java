package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;

import java.util.Optional;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Integer> {
    Optional<DoctorEntity> findByClinicUserId(Integer userId);
}
