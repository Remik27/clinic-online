package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.FreeTermDao;
import pl.zajavka.domain.FreeTerm;

import java.util.List;

@Service
@AllArgsConstructor
public class FreeTermService {

    private final FreeTermDao freeTermDao;
    @Transactional
    public void addFreeTerms(List<FreeTerm> freeTerms){
        freeTermDao.saveAll(freeTerms);
    }

}
