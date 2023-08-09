package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.PatientDao;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.infrastructure.db.repository.jpa.PatientJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.PatientMapper;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDao {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientMapper patientMapper;

    @Override
    public Patient findPatient(Patient patient) {
        PatientEntity byId = patientJpaRepository.findById(patient.getId())
                .orElseThrow(()-> new NotFoundException("Patient [%d] not found".formatted(patient.getId())));
                return patientMapper.mapFromEntity(byId);

    }

    @Override
    public Patient addPatient(Patient patient) {
        PatientEntity patientEntity = patientJpaRepository.saveAndFlush(patientMapper.mapToEntity(patient));
        return patientMapper.mapFromEntity(patientEntity);
    }

    @Override
    public boolean checkExistencePatient(Patient patient) {
        return patientJpaRepository.findByPesel(patient.getPesel()).isPresent();
    }
}
