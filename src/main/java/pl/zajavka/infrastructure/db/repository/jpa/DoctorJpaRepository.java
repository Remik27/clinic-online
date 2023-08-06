package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Integer> {
}
