package pl.zajavka.infrastructure.db.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;

import java.util.List;

@Repository
public interface FreeTermJpaRepository extends JpaRepository<FreeTermEntity, Integer> {
    @Query("""
            SELECT term FROM FreeTermEntity term
            WHERE term.doctor.specialization = :specialization
            """)
    List<FreeTermEntity> getFreeTermsBySpecialization(final @Param("specialization") String specialization);
}
