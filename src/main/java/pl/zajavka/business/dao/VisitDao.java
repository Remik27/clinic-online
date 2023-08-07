package pl.zajavka.business.dao;

import pl.zajavka.domain.Visit;

public interface VisitDao {
    Visit updateVisit(Visit visit);

    Visit findById(Integer visitId);
}
