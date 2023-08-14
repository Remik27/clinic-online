package pl.zajavka.infrastructure.db.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.VisitEntity;

import java.util.List;

@Repository
public interface VisitJpaRepository extends JpaRepository<VisitEntity, Integer> {
    List<VisitEntity> findByPatientId(Integer patientId);


    @Query("""
            SELECT visit FROM VisitEntity visit
            WHERE visit.patient.id = :patientId AND visit.status = 'DONE'
            ORDER BY visit.term ASC
            """)
    List<VisitEntity> getListDoneVisit(final @Param("patientId") Integer patientId);

    @Query("""
            SELECT visit FROM VisitEntity visit
            WHERE visit.patient.id = :patientId AND visit.status = 'UPCOMING'
            ORDER BY visit.term DESC
            """)
    List<VisitEntity> getListFutureVisit(final @Param("patientId") Integer patientId);
}
