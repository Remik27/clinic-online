package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.infrastructure.db.repository.jpa.DoctorJpaRepository;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.DoctorEntityMapper;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDao {

    private final FreeTermEntityMapper freeTermEntityMapper;
    private final DoctorEntityMapper doctorEntityMapper;
    private final FreeTermJpaRepository freeTermJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    @Override
    public List<FreeTerm> saveAllTerms(Doctor doctor) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        List<FreeTermEntity> freeTermEntities = new ArrayList<>();

        doctor.getFreeTerms().stream()
                .filter(term -> Objects.isNull(term.getId()))
                .map(freeTermEntityMapper::mapToEntity)
                .forEach(
                    freeTermEntity -> {
                        freeTermEntity.setDoctor(doctorEntity);
                        freeTermEntities.add(freeTermEntity);
                    }
                );
        return freeTermJpaRepository.saveAllAndFlush(freeTermEntities)
                .stream().map(freeTermEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        DoctorEntity saved = doctorJpaRepository.saveAndFlush(doctorEntity);
        return doctorEntityMapper.mapFromEntity(saved);
    }

    @Override
    public Doctor findDoctorByClinicUserId(Integer userId) {
        DoctorEntity doctorEntity = doctorJpaRepository.findByClinicUserId(userId)
                .orElseThrow(()-> new NotFoundException("Doctor with user id [%s] not found".formatted(userId)));
        return doctorEntityMapper.mapFromEntity(doctorEntity);
    }
}
