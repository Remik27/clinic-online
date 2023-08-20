package pl.zajavka.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.business.dao.PatientDao;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.AlreadyExistException;
import pl.zajavka.domain.exception.NotFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static pl.zajavka.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientDao patientDao;

    @Mock
    private VisitService visitService;

    @InjectMocks
    private PatientService patientService;

    @Test
    void diseaseHistoryCanBeShowCorrectly() {
        //given
        Patient patient = somePatient().withId(1);
        List<Visit> visits = List.of(someVisit1().withPatient(patient), someVisit2().withPatient(patient));
        TreeMap<OffsetDateTime, String> mapDisease = new TreeMap<>(
                visits.stream().collect(Collectors.toMap(Visit::getTerm, Visit::getDisease)));

        //when
        Mockito.when(patientDao.findPatient(patient)).thenReturn(patient);
        Mockito.when(visitService.findVisitsByPatientIdAndStatus(patient.getId(), Visit.Status.DONE))
                .thenReturn(visits);

        DiseaseHistory diseaseHistory = patientService.showDiseaseHistory(patient);

        //then
        Assertions.assertEquals(mapDisease.size(), diseaseHistory.getDiseaseHistory().size());
        Assertions.assertEquals(patient, diseaseHistory.getPatient());
    }

    @Test
    void showDiseaseHistoryShouldThrownWhenPatientIsNotFound() {
        //given
        Patient patient = somePatient().withId(1);
        String message = "Patient [%d] not found".formatted(patient.getId());

        //when
        Mockito.when(patientDao.findPatient(patient)).thenReturn(null);
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> patientService.showDiseaseHistory(patient));

        //then
        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    void patientCanBeAddedCorrectly(){
        //given
        Patient patient = somePatient();
        //when
        Mockito.when(patientDao.checkExistencePatient(patient)).thenReturn(false);
        Mockito.when(patientDao.addPatient(patient)).thenReturn(patient.withId(1));

        Patient savedPatient = patientService.addPatient(patient);
        //then
        Assertions.assertTrue(Objects.nonNull(savedPatient.getId()));
        Assertions.assertEquals(patient, savedPatient);

    }
    @Test
    void addPatientShouldThrowWhenPatientAlreadyExist(){
        //given
        Patient patient = somePatient();
        String message = "Patient with pesel [%s] already exist".formatted(patient.getPesel());
        //when
        Mockito.when(patientDao.checkExistencePatient(patient)).thenReturn(true);
        AlreadyExistException exception = Assertions.assertThrows(AlreadyExistException.class, () -> patientService.addPatient(patient));
        //then
        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    void findPatientByPeselCanReturnPatientCorrectly(){
        //given
        String pesel = "11111111111";
        Patient patient = somePatient().withPesel(pesel);

        //when
        Mockito.when(patientDao.findPatientByPesel(pesel)).thenReturn(patient);

        Patient patientByPesel = patientService.findPatientByPesel(pesel);

        //then

        Assertions.assertEquals(patient, patientByPesel);
    }
    @Test
    void findPatientByClinicUserIdCanReturnPatientCorrectly(){
        //given
        Integer clinicUserId = 1;
        Patient patient = somePatient().withClinicUserId(clinicUserId);

        //when
        Mockito.when(patientDao.findPatientByClinicUserId(clinicUserId)).thenReturn(patient);

        Patient patientByClinicUserId = patientService.findPatientByClinicUserId(clinicUserId);

        //then

        Assertions.assertEquals(patient, patientByClinicUserId);
    }
    @Test
    void findPatientByVisitIdCanReturnPatientCorrectly(){
        //given
        Integer visitId = 1;
        Patient patient = somePatient();

        //when
        Mockito.when(patientDao.findPatientByVisitId(visitId)).thenReturn(patient);

        Patient patientByVisitId = patientService.findPatientByVisitId(visitId);

        //then

        Assertions.assertEquals(patient, patientByVisitId);
    }
    @Test
    void updatePatientCanUpdatePatientCorrectly(){
        //given
        Patient patient = somePatient();
        Patient expectedPatient = patient.withName("other");

        //when
        Mockito.when(patientDao.updatePatient(expectedPatient)).thenReturn(expectedPatient);

        Patient actualPatient = patientService.updatePatient(expectedPatient);

        //then

        Assertions.assertNotEquals(patient.getName(), actualPatient.getName());
        Assertions.assertEquals(expectedPatient.getName(), actualPatient.getName());
        Assertions.assertEquals(patient, actualPatient);
        Assertions.assertEquals(expectedPatient, actualPatient);
    }


}