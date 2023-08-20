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
import java.util.Comparator;
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
        Visit visit = someVisit1().withStatus(Visit.Status.UPCOMING);
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
                Arguments.of(someVisit1().withStatus(Visit.Status.UPCOMING), false),
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
                Arguments.of(someVisit1().withStatus(Visit.Status.UPCOMING), false),
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
    @Test
    void getListVisitCanBeReturnCorrectly(){
        //given
        Patient patient = somePatient().withId(1);
        List<Visit> visitsDone = new ArrayList<>(List.of(someVisit1(), someVisit2()));
        visitsDone.sort(Comparator.comparing(Visit::getTerm));
        List<Visit> visitsFuture = new ArrayList<>(List.of(someVisit3(), someVisit4()));
        visitsDone.sort(Comparator.comparing(Visit::getTerm).reversed());
        //when
        Mockito.when(visitDao.getListDoneVisit(patient))
                .thenReturn(visitsDone);
        Mockito.when(visitDao.getListUpcomingVisit(patient))
                .thenReturn(visitsFuture);

        List<Visit> doneVisits = visitService.getListVisit(patient, Visit.Status.DONE);
        List<Visit> futureVisits = visitService.getListVisit(patient, Visit.Status.UPCOMING);
        //then

        Assertions.assertEquals(2, doneVisits.size());
        Assertions.assertEquals(2, futureVisits.size());
        Assertions.assertTrue(doneVisits.get(0).getTerm().isAfter( doneVisits.get(1).getTerm()));
        Assertions.assertTrue(futureVisits.get(0).getTerm().isBefore( futureVisits.get(1).getTerm()));
    }

    @Test
    void cancelVisitCanCancelCorrectly(){
        //given
        Visit visit = someVisit3().withId(1);
        Visit visitExpected = visit.withStatus(Visit.Status.CANCELLED);
        //when
        Mockito.when(visitDao.findById(1)).thenReturn(visit);
        Mockito.when(visitDao.updateVisit(visitExpected)).thenReturn(visitExpected);

        Visit cancelledVisit = visitService.cancelVisit(visit.getId());
        //then
        Assertions.assertEquals(Visit.Status.CANCELLED, cancelledVisit.getStatus());
    }
    @Test
    void findVisitsByDoctorIdCanReturnVisitsCorrectly(){
        //given
        Integer id = 1;
        List<Visit> visitsDone = new ArrayList<>(List.of(someVisit1(), someVisit2()));
        visitsDone.sort(Comparator.comparing(Visit::getTerm));
        List<Visit> visitsFuture = new ArrayList<>(List.of(someVisit3(), someVisit4()));
        visitsDone.sort(Comparator.comparing(Visit::getTerm).reversed());
        //when
        Mockito.when(visitDao.findDoneVisitsByDoctorId(id))
                .thenReturn(visitsDone);
        Mockito.when(visitDao.findUpcomingVisitsByDoctorId(id))
                .thenReturn(visitsFuture);

        List<Visit> doneVisits = visitService.findVisitsByDoctorId(id, Visit.Status.DONE);
        List<Visit> futureVisits = visitService.findVisitsByDoctorId(id, Visit.Status.UPCOMING);
        //then

        Assertions.assertEquals(2, doneVisits.size());
        Assertions.assertEquals(2, futureVisits.size());
        Assertions.assertTrue(doneVisits.get(0).getTerm().isAfter( doneVisits.get(1).getTerm()));
        Assertions.assertTrue(futureVisits.get(0).getTerm().isBefore( futureVisits.get(1).getTerm()));
    }

    @Test
    void findVisitsByPatientIdAndStatusCanFoundVisitsCorrectly() {
        //given
        Patient patient1 = somePatient().withId(1);
        Patient patient2 = somePatient().withId(2);
        Visit visit1 = someVisit1().withPatient(patient1).withStatus(Visit.Status.DONE);
        Visit visit2 = someVisit2().withPatient(patient1).withStatus(Visit.Status.UPCOMING);
        List<Visit> visits = List.of(visit1, visit2);

        //when
        Mockito.when(visitDao
                .findVisitsByPatientIdAndStatus(patient1.getId(), Visit.Status.DONE)).thenReturn(List.of(visit1));
        Mockito.when(visitDao
                .findVisitsByPatientIdAndStatus(patient1.getId(), Visit.Status.UPCOMING)).thenReturn(List.of(visit2));
        Mockito.when(visitDao
                .findVisitsByPatientIdAndStatus(patient2.getId(), Visit.Status.DONE)).thenReturn(new ArrayList<>());
        Mockito.when(visitDao
                .findVisitsByPatientIdAndStatus(patient2.getId(), Visit.Status.UPCOMING)).thenReturn(new ArrayList<>());

        List<Visit> visitsByPatient1Done =
                visitService.findVisitsByPatientIdAndStatus(patient1.getId(), Visit.Status.DONE);
        List<Visit> visitsByPatient1Upcoming =
                visitService.findVisitsByPatientIdAndStatus(patient1.getId(), Visit.Status.UPCOMING);
        List<Visit> visitsByPatient2Done =
                visitService.findVisitsByPatientIdAndStatus(patient2.getId(), Visit.Status.DONE);
        List<Visit> visitsByPatient2Upcoming =
                visitService.findVisitsByPatientIdAndStatus(patient2.getId(), Visit.Status.UPCOMING);

        //then

        Assertions.assertTrue(visits.size() > visitsByPatient1Done.size());
        Assertions.assertTrue(visits.size() > visitsByPatient1Upcoming.size());
        Assertions.assertTrue(visitsByPatient2Done.isEmpty());
        Assertions.assertTrue(visitsByPatient2Upcoming.isEmpty());
        Assertions.assertNotEquals(visitsByPatient1Done.get(0), visitsByPatient1Upcoming.get(0));

    }
    @Test
    void addDiseaseCanBeUpdateCorrectly() {
        //given
        Visit visit = someVisit3();
        String disease = "some disease";
        Visit visitExpected = visit.withDisease(disease);

        //when
        Mockito.when(visitDao.updateVisit(visitExpected)).thenReturn(visitExpected);
        Visit visitUpdated = visitService.addDisease(visit, disease);

        //then

        Assertions.assertEquals(disease, visitUpdated.getDisease());
        Assertions.assertEquals(visitExpected, visitUpdated);
    }
    @Test
    void finishVisitCanFinishCorrectly(){
        //given
        Visit visit = someVisit3().withId(1);
        Visit visitExpected = visit.withStatus(Visit.Status.DONE);
        //when
        Mockito.when(visitDao.findById(1)).thenReturn(visit);
        Mockito.when(visitDao.updateVisit(visitExpected)).thenReturn(visitExpected);

        Visit cancelledVisit = visitService.finishVisit(visit.getId().toString());
        //then
        Assertions.assertEquals(Visit.Status.DONE, cancelledVisit.getStatus());
    }

    @Test
    void findCancelledVisitsCanReturnVisitsCorrectly(){
        //given
        List<Visit> visits = List.of(someVisit1().withStatus(Visit.Status.CANCELLED),
                someVisit2().withStatus(Visit.Status.CANCELLED));
        //when
        Mockito.when(visitDao.getListCancelledVisits()).thenReturn(visits);

        List<Visit> cancelledVisits = visitService.findCancelledVisits();
        //then
        Assertions.assertEquals(visits.size(), cancelledVisits.size());
    }

}