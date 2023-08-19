package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.VisitDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.WrongStatusException;

import java.util.List;

@AllArgsConstructor
@Service
public class VisitService {

    private final VisitDao visitDao;

    @Transactional
    public Visit addDescriptionAndChangeStatus(Visit visit, String description) {
        return visitDao.updateVisit(visit.withDescription(description).withStatus(Visit.Status.DONE));
    }

    public Visit findVisitById(Integer visitId) {
        return visitDao.findById(visitId);
    }

    public boolean isDone(Visit visit) {
        return Visit.Status.DONE.equals(visit.getStatus());
    }

    @Transactional
    public Visit addDescription(Visit visit, String description) {
        return visitDao.updateVisit(visit.withDescription(description));
    }

    public boolean isCancelled(Visit visit) {
        return Visit.Status.CANCELLED.equals(visit.getStatus());
    }

    public List<Visit> findVisitsByPatientId(Integer patientId) {
        return visitDao.findVisitsByPatientId(patientId);
    }

    @Transactional
    public Visit buildVisit(Patient patient, FreeTerm freeTerm) {
        Visit visit = Visit.builder()
                .patient(patient)
                .doctor(freeTerm.getDoctor())
                .term(freeTerm.getTerm())
                .status(Visit.Status.UPCOMING)
                .build();
        return visitDao.saveVisit(visit);
    }

    public List<Visit> getListVisit(Patient patient, Visit.Status status) {
        if (Visit.Status.DONE.equals(status)) {
            return visitDao.getListDoneVisit(patient);
        } else if (Visit.Status.UPCOMING.equals(status)) {
            return visitDao.getListUpcomingVisit(patient);
        }
        throw new WrongStatusException("Status must be DONE or UPCOMING not [%s]".formatted(status.toString()));
    }

    @Transactional
    public Visit cancelVisit(Integer visitId) {
        Visit visit = findVisitById(visitId);
        Visit visitCancelled = visit.withStatus(Visit.Status.CANCELLED);
        return visitDao.updateVisit(visitCancelled);
    }


    public List<Visit> findUpcomingVisitsByDoctorId(Integer id, Visit.Status status) {
        return switch (status) {
            case UPCOMING -> visitDao.findUpcomingVisitsByDoctorId(id);
            case DONE -> visitDao.findDoneVisitsByDoctorId(id);
            default -> throw new WrongStatusException("Status [%s] is wrong".formatted(status));
        };
    }

    public List<Visit> findVisitsByPatientIdAndStatus(Integer id, Visit.Status status) {
        return visitDao.findVisitsByPatientIdAndStatus(id, status);
    }

    public Visit addDisease(Visit visit, String disease) {
        return visitDao.updateVisit(visit.withDisease(disease));
    }

    public Visit finishVisit(String visitId) {
        Visit visit = visitDao.findById(Integer.valueOf(visitId));
        return visitDao.updateVisit(visit.withStatus(Visit.Status.DONE));
    }

    public List<Visit> findVisitsByStatus(Visit.Status status) {
        return visitDao.getListCancelledVisits();
    }

    public void delete(List<Visit> visits) {
        visitDao.delete(visits);
    }
}

