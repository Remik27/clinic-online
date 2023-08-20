package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.business.dao.NFZTermsDao;
import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.zajavka.util.DomainFixtures.someFromNfz;
import static pl.zajavka.util.DomainFixtures.someTermFromNfz;

@ExtendWith(MockitoExtension.class)
class NFZServiceTest {

    @Mock
    NFZTermsDao nfzTermsDao;

    @InjectMocks
    NFZService nfzService;


    @Test
    void getAvailableBenefitsCanReturnBenefitsCorrectly() {
        //given
        String benefit = "some";
        List<String> benefits = List.of("some benefit", "some benefits");

        //when
        Mockito.when(nfzTermsDao.getAvailableBenefits(benefit)).thenReturn(benefits);

        List<String> availableBenefits = nfzService.getAvailableBenefits(benefit);

        //then
        Assertions.assertEquals(benefits.size(), availableBenefits.size());
    }

    @Test
    void getAvailableBenefitsShouldThrowWhenListIsEmpty() {
        //given
        String benefit = "some";
        String exceptedMessage = "Received benefit [%s] not found in NFZ dictionary".formatted(benefit);
        List<String> benefits = new ArrayList<>();

        //when
        Mockito.when(nfzTermsDao.getAvailableBenefits(benefit)).thenReturn(benefits);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> nfzService.getAvailableBenefits(benefit));
        //then
            Assertions.assertEquals(exceptedMessage, exception.getMessage());
    }

    @Test
    void getFirstTermCanReturnBenefitsCorrectly() {
        //given
        FormNfz formAttributes = someFromNfz();
        TermFromNfz exceptedTerm = someTermFromNfz();

        //when
        Mockito.when(nfzTermsDao.getFirstTerm(formAttributes)).thenReturn(Optional.of(exceptedTerm));

        TermFromNfz actualTerm = nfzService.getFirstTerm(formAttributes);

        //then
        Assertions.assertEquals(exceptedTerm, actualTerm);
    }

    @Test
    void getFirstTermShouldThrowWhenListIsEmpty() {
        //given
        FormNfz formAttributes = someFromNfz();
        String exceptedMessage = "Term with received parameters not found";

        //when
        Mockito.when(nfzTermsDao.getFirstTerm(formAttributes)).thenReturn(Optional.empty());

        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> nfzService.getFirstTerm(formAttributes));
        //then
            Assertions.assertEquals(exceptedMessage, exception.getMessage());
    }

}