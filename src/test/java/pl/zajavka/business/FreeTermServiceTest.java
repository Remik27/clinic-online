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
    void bookTermShouldThrowWhenTermNotExist(){
        //given
        Patient patient = somePatient();
        FreeTerm freeTerm = someTerm1().withId(1);
        String message = "Free Term with id [%d] not found".formatted(freeTerm.getId());
        //when
        Mockito.when(freeTermDao.checkAvailabilityOfTerm(freeTerm.getId())).thenReturn(false);
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, ()->freeTermService.bookTerm(patient, freeTerm));
        //then
        Assertions.assertEquals(message, exception.getMessage());
    }
}