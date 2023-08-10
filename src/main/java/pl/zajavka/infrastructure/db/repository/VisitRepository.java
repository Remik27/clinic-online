package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.VisitDao;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.VisitEntity;
import pl.zajavka.infrastructure.db.repository.jpa.VisitJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.VisitEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class VisitRepository implements VisitDao {

    private final VisitEntityMapper visitEntityMapper;
    private final VisitJpaRepository visitJpaRepository;

    @Override
    public Visit updateVisit(Visit visit) {
        VisitEntity visitEntity = visitEntityMapper.mapToEntity(visit);
        VisitEntity saved = visitJpaRepository.saveAndFlush(visitEntity);
        return visitEntityMapper.mapFromEntity(saved);
    }

    @Override
    public Visit findById(Integer visitId) {
        VisitEntity visitEntity = visitJpaRepository.findById(visitId)
                .orElseThrow(() -> new NotFoundException("Not found visit with id [%d]".formatted(visitId)));

        return visitEntityMapper.mapFromEntity(visitEntity);
    }

    @Override
    public List<Visit> findVisitsByPatientId(Integer patientId) {
        return visitJpaRepository.findByPatientId(patientId).stream()
                .map(visitEntityMapper::mapFromEntity)
                .toList();

    }

    @Override
    public Visit saveVisit(Visit visit) {
        VisitEntity visitEntity = visitEntityMapper.mapToEntity(visit);
        VisitEntity visitSaved = visitJpaRepository.saveAndFlush(visitEntity);
        return visitEntityMapper.mapFromEntity(visitSaved);
    }

    @Override
    public List<Visit> getListDoneVisit(Patient patient) {
        List<VisitEntity> listDoneVisit = visitJpaRepository.getListDoneVisit(patient.getId());
        return listDoneVisit.stream().map(visitEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<Visit> getListFutureVisit(Patient patient) {
        List<VisitEntity> listFutureVisit = visitJpaRepository.getListFutureVisit(patient.getId());
        return listFutureVisit.stream().map(visitEntityMapper::mapFromEntity).toList();
    }
}
