package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.VisitDescription;
import pl.zajavka.domain.exception.UpdatingCancelledVisitException;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorDao doctorDao;
    private final VisitService visitService;

    @Transactional
    public void saveTerms(Doctor doctor){
        doctorDao.saveAllTerms(doctor);
    }

    @Transactional
    public Doctor saveDoctor(Doctor doctor){
        return doctorDao.saveDoctor(doctor);
    }

    @Transactional
    public Visit addDescriptionToDoneVisit(VisitDescription visitDescription){
        Visit visit = visitService.findVisitById(visitDescription.getVisitId());
        if (visitService.isDone(visit)){
            return visitService.addDescription(visit, visitDescription.getDescription());
        } else if (visitService.isCancelled(visit)) {
            throw new UpdatingCancelledVisitException("Visit [%d] is cancelled".formatted(visit.getId()));
        } else {
            return visitService.addDescriptionAndChangeStatus(visit, visitDescription.getDescription());
        }

    }

}
