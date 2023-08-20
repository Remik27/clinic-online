package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.NFZTermsDao;
import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class NFZService {
private final NFZTermsDao nfzTermsDao;

    public List<String> getAvailableBenefits(String benefit) {
        List<String> availableBenefits = nfzTermsDao.getAvailableBenefits(benefit);
        if (availableBenefits.isEmpty()){
            throw new NotFoundException("Received benefit [%s] not found in NFZ dictionary".formatted(benefit));
        }
        return availableBenefits;
    }

    public TermFromNfz getFirstTerm(FormNfz formAttributes) {
       return nfzTermsDao.getFirstTerm(formAttributes)
               .orElseThrow(()-> new NotFoundException("Term with received parameters not found"));
    }
}
