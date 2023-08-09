package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.FreeTermDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class FreeTermService {

    private final FreeTermDao freeTermDao;
    private final VisitService visitService;
    @Transactional
    public void addFreeTerms(List<FreeTerm> freeTerms){
        freeTermDao.saveAll(freeTerms);
    }

    @Transactional
    public synchronized Visit bookTerm(Patient patient, FreeTerm freeTerm){
        if (checkAvailabilityOdTerm(freeTerm)){
            freeTermDao.delete(freeTerm);
            return visitService.buildVisit(patient, freeTerm);
        }else {
            throw new NotFoundException("Free Term with id [%d] not found".formatted(freeTerm.getId()));
        }
    }

    private boolean checkAvailabilityOdTerm(FreeTerm freeTerm) {
        return freeTermDao.checkAvailabilityOfTerm(freeTerm.getId());
    }

}
