package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.PatientEntity;

import java.util.Optional;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Integer> {
    Optional<PatientEntity> findByPesel(String pesel);

    Optional<PatientEntity> findByClinicUserId(Integer userId);

    @Query("""
            SELECT patient
            FROM PatientEntity patient
            WHERE patient.id = (
                SELECT MAX(p.id)
                FROM VisitEntity visit
                JOIN visit.patient p
                WHERE visit.id = :visitId
            )
          
            """)
    PatientEntity findPatientByVisitId(final @Param("visitId") Integer visitId);
}
