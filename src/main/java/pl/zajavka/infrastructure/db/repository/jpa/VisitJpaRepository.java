package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.VisitEntity;

import java.util.List;

@Repository
public interface VisitJpaRepository extends JpaRepository<VisitEntity, Integer> {
    List<VisitEntity> findByPatientId(Integer patientId);
}
