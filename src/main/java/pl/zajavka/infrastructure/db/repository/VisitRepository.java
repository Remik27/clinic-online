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
    public List<Visit> getListUpcomingVisit(Patient patient) {
        List<VisitEntity> listUpcomingVisit = visitJpaRepository.getListUpcomingVisit(patient.getId());
        return listUpcomingVisit.stream().map(visitEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<Visit> findUpcomingVisitsByDoctorId(Integer id) {
        List<VisitEntity> listUpcomingVisit = visitJpaRepository.getListUpcomingVisitByDoctor(id);
        return listUpcomingVisit.stream().map(visitEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<Visit> findDoneVisitsByDoctorId(Integer id) {
        List<VisitEntity> listUpcomingVisit = visitJpaRepository.getListDoneVisitByDoctor(id);
        return listUpcomingVisit.stream().map(visitEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<Visit> findVisitsByPatientIdAndStatus(Integer patientId, Visit.Status status) {
        List<VisitEntity> entities = visitJpaRepository.findByPatientIdAndStatus(patientId, status.toString());
        return entities.stream().map(visitEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<Visit> getListCancelledVisits() {
        return visitJpaRepository.findByStatus(Visit.Status.CANCELLED.name()).stream()
                .map(visitEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public void delete(List<Visit> visits) {
        List<VisitEntity> entities = visits.stream().map(visitEntityMapper::mapToEntity).toList();
        visitJpaRepository.deleteAll(entities);
    }


}
