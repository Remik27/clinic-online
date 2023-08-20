package pl.zajavka.business.dao;

import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;

import java.util.List;
import java.util.Optional;

public interface NFZTermsDao {
    List<String> getAvailableBenefits(String benefit);

    Optional<TermFromNfz> getFirstTerm(FormNfz formAttributes);
}
