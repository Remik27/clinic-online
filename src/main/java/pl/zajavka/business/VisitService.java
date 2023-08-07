package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.VisitDao;
import pl.zajavka.domain.Visit;
@AllArgsConstructor
@Service
public class VisitService {

    private final VisitDao visitDao;
    public Visit addDescriptionAndChangeStatus(Visit visit, String description) {
        return visitDao.updateVisit(visit.withDescription(description).withStatus(Visit.Status.DONE));
    }

    public Visit findVisitById(Integer visitId) {
        return visitDao.findById(visitId);
    }

    public boolean isDone(Visit visit) {
        return Visit.Status.DONE.equals(visit.getStatus());
    }

    public Visit addDescription(Visit visit, String description) {
        return visitDao.updateVisit(visit.withDescription(description));
    }

    public boolean isCancelled(Visit visit) {
        return Visit.Status.CANCELLED.equals(visit.getStatus());
    }
}
