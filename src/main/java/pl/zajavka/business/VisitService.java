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
                .status(Visit.Status.FUTURE)
                .build();
        return visitDao.saveVisit(visit);
    }

    public List<Visit> getListVisit(Patient patient, Visit.Status status) {
        if (Visit.Status.DONE.equals(status)){
            return visitDao.getListDoneVisit(patient);
        } else if (Visit.Status.FUTURE.equals(status)) {
            return visitDao.getListFutureVisit(patient);
        }
        throw new WrongStatusException("Status must be DONE or FUTURE not [%s]".formatted(status.toString()));
    }
@Transactional
    public Visit cancelVisit(Visit visit) {
        Visit visitCancelled = visit.withStatus(Visit.Status.CANCELLED);
        return visitDao.updateVisit(visitCancelled);
    }
}

