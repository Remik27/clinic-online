package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.business.dao.DoctorDao;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.VisitDescription;
import pl.zajavka.domain.exception.UpdatingCancelledVisitException;

import java.util.List;
import java.util.Set;

import static pl.zajavka.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private VisitService visitService;
    @Mock
    private DoctorDao doctorDao;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void freeTermsCanBeSavedCorrectly(){
        //given
        Set<FreeTerm> freeTerms = Set.of(someTerm1(), someTerm2(), someTerm3());
        Doctor doctor = someDoctor1().withFreeTerms(freeTerms);
        List<FreeTerm> freeTermList = List.copyOf(freeTerms);
        //when
        Mockito.when(doctorDao.saveAllTerms(doctor)).thenReturn(freeTermList);
        List<FreeTerm> savedFreeTerms = doctorService.saveTerms(doctor);
        //then
        Assertions.assertEquals(freeTerms.size(), savedFreeTerms.size());
    }

    @Test
    void doctorCanBeSavedCorrectly(){
        //given
        Doctor doctor = someDoctor1();
        //when
        Mockito.when(doctorDao.saveDoctor(doctor)).thenReturn(doctor);
        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        //then
        Assertions.assertEquals(doctor, savedDoctor);
    }

    @Test
    void visitDescriptionCanBeAddedCorrectlyWhenVisitIsDone(){
        //given
        VisitDescription visitDescription = someVisitDescription();
        Visit visit = someVisit1();

        //when
        Mockito.when(visitService.findVisitById(visitDescription.getVisitId())).thenReturn(visit);
        Mockito.when(visitService.isDone(visit)).thenReturn(true);
        Mockito.when(visitService.addDescription(visit,visitDescription.getDescription()))
                .thenReturn(visit.withDescription(someVisitDescription().getDescription()));
        Visit visitWithDescription = doctorService.addDescriptionToVisit(visitDescription);

        //then
        Assertions.assertNotNull(visitWithDescription.getDescription());
        Assertions.assertNotEquals(visit, visitWithDescription);


    }
    @Test
    void visitDescriptionShouldThrownWhenVisitIsCancelled(){
        //given
        VisitDescription visitDescription = someVisitDescription();
        Visit visit = someVisit1().withStatus(Visit.Status.CANCELLED).withId(visitDescription.getVisitId());
        String message = "Visit [%d] is cancelled".formatted(visitDescription.getVisitId());

        //when
        Mockito.when(visitService.findVisitById(visitDescription.getVisitId())).thenReturn(visit);
        Mockito.when(visitService.isDone(visit)).thenReturn(false);
        Mockito.when(visitService.isCancelled(visit)).thenReturn(true);
        UpdatingCancelledVisitException exception = Assertions.assertThrows(UpdatingCancelledVisitException.class,
                () -> doctorService.addDescriptionToVisit(visitDescription));

        //then
        Assertions.assertEquals(message, exception.getMessage());


    }
    @Test
    void visitDescriptionCanBeAddedCorrectlyAndUpdateStatusWhenVisitIsFuture(){
        //given
        VisitDescription visitDescription = someVisitDescription();
        Visit visit = someVisit1().withStatus(Visit.Status.FUTURE).withId(visitDescription.getVisitId());

        //when
        Mockito.when(visitService.findVisitById(visitDescription.getVisitId())).thenReturn(visit);
        Mockito.when(visitService.isDone(visit)).thenReturn(false);
        Mockito.when(visitService.isCancelled(visit)).thenReturn(false);
        Mockito.when(visitService.addDescriptionAndChangeStatus(visit, visitDescription.getDescription()))
                .thenReturn(visit.withDescription(visitDescription.getDescription()).withStatus(Visit.Status.DONE));
        Visit visitWithDecription = doctorService.addDescriptionToVisit(visitDescription);


        //then
        Assertions.assertNotEquals(visit.getStatus(), visitWithDecription.getStatus());
        Assertions.assertNotNull(visitWithDecription.getDescription());
        Assertions.assertEquals(Visit.Status.DONE, visitWithDecription.getStatus());


    }

}