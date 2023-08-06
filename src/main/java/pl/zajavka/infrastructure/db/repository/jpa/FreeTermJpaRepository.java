package pl.zajavka.infrastructure.db.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;

@Repository
public interface FreeTermJpaRepository extends JpaRepository<FreeTermEntity, Integer> {
}
