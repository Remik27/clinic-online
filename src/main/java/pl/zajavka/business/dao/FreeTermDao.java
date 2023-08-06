package pl.zajavka.business.dao;

import pl.zajavka.domain.FreeTerm;

import java.util.List;

public interface FreeTermDao {
    void saveAll(List<FreeTerm> freeTerms);
}
