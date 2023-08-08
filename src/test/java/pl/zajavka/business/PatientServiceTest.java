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
import pl.zajavka.domain.exception.NotFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
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
        List<Visit> visits = List.of(someVisit1(), someVisit2());
        Map<OffsetDateTime, String> mapDisease =
                visits.stream().collect(Collectors.toMap(Visit::getTerm, Visit::getDisease));

        //when
        Mockito.when(visitService.findVisitsByPatientId(patient.getId()))
                .thenReturn(visits);
        Mockito.when(patientDao.findPatient(patient)).thenReturn(patient);
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
}