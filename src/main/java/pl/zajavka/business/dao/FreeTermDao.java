package pl.zajavka.business.dao;

import pl.zajavka.domain.FreeTerm;

import java.util.List;

public interface FreeTermDao {
    List<FreeTerm> saveAll(List<FreeTerm> freeTerms);


    boolean checkAvailabilityOfTerm(Integer id);

    void delete(FreeTerm freeTerm);

    List<FreeTerm> getTermsBySpecialization(String specialization);

    FreeTerm getTerm(Integer freeTermId);
}
