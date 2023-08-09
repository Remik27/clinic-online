package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.business.dao.VisitDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static pl.zajavka.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitDao visitDao;

    @InjectMocks
    private VisitService visitService;

    @Test
    void addDescriptionAndChangeStatusCanBeUpdateCorrectly() {
        //given
        Visit visit = someVisit1().withStatus(Visit.Status.FUTURE);
        String description = "some description";
        Visit visitExpected = visit.withDescription(description).withStatus(Visit.Status.DONE);

        //when
        Mockito.when(visitDao.updateVisit(visitExpected)).thenReturn(visitExpected);
        Visit visitUpdated = visitService.addDescriptionAndChangeStatus(visit, description);

        //then

        Assertions.assertEquals(Visit.Status.DONE, visitUpdated.getStatus());
        Assertions.assertEquals(description, visitUpdated.getDescription());
        Assertions.assertEquals(visitExpected, visitUpdated);
    }

    @Test
    void addDescriptionCanBeUpdateCorrectly() {
        //given
        Visit visit = someVisit1();
        String description = "some description";
        Visit visitExpected = visit.withDescription(description);

        //when
        Mockito.when(visitDao.updateVisit(visitExpected)).thenReturn(visitExpected);
        Visit visitUpdated = visitService.addDescription(visit, description);

        //then

        Assertions.assertEquals(description, visitUpdated.getDescription());
        Assertions.assertEquals(visitExpected, visitUpdated);
    }

    @Test
    void findVisitByIdCanBeFoundCorrectly() {
        //given
        Visit visit = someVisit1().withId(1);

        //when
        Mockito.when(visitDao.findById(1)).thenReturn(visit);
        Visit visitById = visitService.findVisitById(1);

        //then
        Assertions.assertEquals(visit, visitById);
    }

    @ParameterizedTest
    @MethodSource("isDoneData")
    void isDoneCanCheckStatusCorrectly(Visit visit, boolean expect) {
        //when
        boolean done = visitService.isDone(visit);
        //then
        Assertions.assertEquals(expect, done);
    }

    public static Stream<Arguments> isDoneData() {
        return Stream.of(
                Arguments.of(someVisit1().withStatus(Visit.Status.DONE), true),
                Arguments.of(someVisit1().withStatus(Visit.Status.FUTURE), false),
                Arguments.of(someVisit1().withStatus(Visit.Status.CANCELLED), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isCancelledData")
    void isCancelledCanCheckStatusCorrectly(Visit visit, boolean expect) {
        //when
        boolean done = visitService.isCancelled(visit);
        //then
        Assertions.assertEquals(expect, done);
    }

    public static Stream<Arguments> isCancelledData() {
        return Stream.of(
                Arguments.of(someVisit1().withStatus(Visit.Status.DONE), false),
                Arguments.of(someVisit1().withStatus(Visit.Status.FUTURE), false),
                Arguments.of(someVisit1().withStatus(Visit.Status.CANCELLED), true)
        );
    }

    @Test
    void findVisitsByPatientIdCanFoundVisitsCorrectly() {
        //given
        Patient patient1 = somePatient().withId(1);
        Patient patient2 = somePatient().withId(2);
        List<Visit> visits = List.of(someVisit1().withPatient(patient1), someVisit2().withPatient(patient1));

        //when
        Mockito.when(visitDao.findVisitsByPatientId(patient1.getId())).thenReturn(visits);
        Mockito.when(visitDao.findVisitsByPatientId(patient2.getId())).thenReturn(new ArrayList<>());

        List<Visit> visitsByPatient1Id = visitService.findVisitsByPatientId(patient1.getId());
        List<Visit> visitsByPatient2Id = visitService.findVisitsByPatientId(patient2.getId());

        //then

        Assertions.assertEquals(visits.size(), visitsByPatient1Id.size());
        Assertions.assertTrue(visitsByPatient2Id.isEmpty());


    }

    @Test
    void buildVisitCanBuildCorrectly() {
        //given
        FreeTerm freeTerm = someTerm1();
        Visit visit = someVisit3()
                .withTerm(someTerm1().getTerm())
                .withDoctor(freeTerm.getDoctor());
        //when
        Mockito.when(visitDao.saveVisit(visit)).thenReturn(visit);
        Visit saved = visitService.buildVisit(visit.getPatient(), freeTerm);
        //then
        Assertions.assertEquals(visit, saved);


    }

}