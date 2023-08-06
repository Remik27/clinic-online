package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.DoctorMapper;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermMapper;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDao {

    private final FreeTermMapper freeTermMapper;
    private final DoctorMapper doctorMapper;
    private final FreeTermJpaRepository freeTermJpaRepository;
    @Override
    public void saveAllTerms(Doctor doctor) {
        DoctorEntity doctorEntity = doctorMapper.mapToEntity(doctor);

        doctor.getFreeTerms().stream()
                .filter(term -> Objects.isNull(term.getId()))
                .map(freeTermMapper::mapToEntity)
                .forEach(
                    freeTermEntity -> {
                        freeTermEntity.setDoctor(doctorEntity);
                        freeTermJpaRepository.saveAndFlush(freeTermEntity);
                    }
                );
    }
}
