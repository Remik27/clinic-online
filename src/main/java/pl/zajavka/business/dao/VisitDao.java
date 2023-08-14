package pl.zajavka.business.dao;

import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.List;

public interface VisitDao {
    Visit updateVisit(Visit visit);

    Visit findById(Integer visitId);

    List<Visit> findVisitsByPatientId(Integer patientId);

    Visit saveVisit(Visit visit);

    List<Visit> getListDoneVisit(Patient patient);

    List<Visit> getListUpcomingVisit(Patient patient);
}
