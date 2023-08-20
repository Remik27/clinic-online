package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.business.dao.FreeTermDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;

import static pl.zajavka.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class FreeTermServiceTest {

    @Mock
    private FreeTermDao freeTermDao;
    @Mock
    private VisitService visitService;
    @InjectMocks
    private FreeTermService freeTermService;

    @Test
    void bookTermCanReserveCorrectly() {
        //given
        Patient patient = somePatient();
        FreeTerm freeTerm = someTerm1().withId(1);
        Visit visit = someVisit1()
                .withPatient(patient)
                .withDoctor(freeTerm.getDoctor())
                .withTerm(freeTerm.getTerm());
        //when
        Mockito.when(freeTermDao.checkAvailabilityOfTerm(freeTerm.getId())).thenReturn(true);
        Mockito.when(visitService.buildVisit(patient, freeTerm)).thenReturn(visit);
        Visit createdVisit = freeTermService.bookTerm(patient, freeTerm);

        //then
        Assertions.assertEquals(visit, createdVisit);
    }

    @Test
    void bookTermShouldThrowWhenTermNotExist() {
        //given
        Patient patient = somePatient();
        FreeTerm freeTerm = someTerm1().withId(1);
        String message = "Free Term with id [%d] not found".formatted(freeTerm.getId());
        //when
        Mockito.when(freeTermDao.checkAvailabilityOfTerm(freeTerm.getId())).thenReturn(false);
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> freeTermService.bookTerm(patient, freeTerm));
        //then
        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    void addFreeTermsCanSaveTermsCorrectly() {
        //given
        List<FreeTerm> freeTerms = List.of(someTerm1(), someTerm2(), someTerm3());

        //when
        Mockito.when(freeTermDao.saveAll(freeTerms)).thenReturn(freeTerms);

        List<FreeTerm> freeTermsReturned = freeTermService.addFreeTerms(freeTerms);

        //then

        Assertions.assertEquals(freeTerms.size(), freeTermsReturned.size());
    }

    @Test
    void getTermsBySpecializationCanReturnTermsCorrectly() {
        //given
        List<FreeTerm> freeTerms = List.of(someTerm1(), someTerm2(), someTerm3());
        String specialization = "OKULISTA";

        //when
        Mockito.when(freeTermDao.getTermsBySpecialization(specialization)).thenReturn(freeTerms);

        List<FreeTerm> termsBySpecialization = freeTermService.getTermsBySpecialization(specialization);

        //then
        Assertions.assertEquals(freeTerms.size(), termsBySpecialization.size());
    }

    @Test
    void getTermCanReturnTermCorrectly() {
        //given
        FreeTerm freeTerm = someTerm1().withId(1);
        Integer id = 1;

        //when
        Mockito.when(freeTermDao.getTerm(id)).thenReturn(freeTerm);

        FreeTerm term = freeTermService.getTerm(id);

        //then

        Assertions.assertEquals(freeTerm.getId(), term.getId());
        Assertions.assertEquals(freeTerm.getTerm(), term.getTerm());
    }
}