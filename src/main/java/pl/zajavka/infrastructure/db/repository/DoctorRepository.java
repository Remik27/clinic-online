package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.repository.jpa.DoctorJpaRepository;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.DoctorMapper;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDao {

    private final FreeTermMapper freeTermMapper;
    private final DoctorMapper doctorMapper;
    private final FreeTermJpaRepository freeTermJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    @Override
    public List<FreeTerm> saveAllTerms(Doctor doctor) {
        DoctorEntity doctorEntity = doctorMapper.mapToEntity(doctor);
        List<FreeTermEntity> freeTermEntities = new ArrayList<>();

        doctor.getFreeTerms().stream()
                .filter(term -> Objects.isNull(term.getId()))
                .map(freeTermMapper::mapToEntity)
                .forEach(
                    freeTermEntity -> {
                        freeTermEntity.setDoctor(doctorEntity);
                        freeTermEntities.add(freeTermEntity);
                    }
                );
        return freeTermJpaRepository.saveAllAndFlush(freeTermEntities)
                .stream().map(freeTermMapper::mapFromEntity).toList();
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        DoctorEntity doctorEntity = doctorMapper.mapToEntity(doctor);
        DoctorEntity saved = doctorJpaRepository.saveAndFlush(doctorEntity);
        return doctorMapper.mapFromEntity(saved);
    }
}
