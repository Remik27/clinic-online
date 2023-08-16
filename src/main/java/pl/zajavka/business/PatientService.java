package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.PatientDao;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.AlreadyExistException;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.security.UserService;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDao patientDao;
    private final VisitService visitService;
    private final UserService userService;

    public DiseaseHistory showDiseaseHistory(Patient patient) {
        if (Objects.isNull(patientDao.findPatient(patient))) {
            throw new NotFoundException("Patient [%d] not found".formatted(patient.getId()));
        }
        List<Visit> visits = visitService.findVisitsByPatientIdAndStatus(patient.getId(), Visit.Status.DONE);
        TreeMap<OffsetDateTime, String> diseasesMap = new TreeMap<>();
        visits.forEach(visit -> diseasesMap.put(visit.getTerm(), visit.getDisease()));
        return DiseaseHistory.builder()
                .patient(patient)
                .diseaseHistory(diseasesMap)
                .build();
    }

    public Patient addPatient(Patient patient) {
        if (!checkExistencePatient(patient)) {
            return patientDao.addPatient(patient);
        } else {
            throw new AlreadyExistException("Patient with pesel [%s] already exist".formatted(patient.getPesel()));
        }
    }

    private boolean checkExistencePatient(Patient patient) {
        return patientDao.checkExistencePatient(patient);
    }

    public Patient findPatientByPesel(String pesel) {
        return patientDao.findPatientByPesel(pesel);
    }


    public Patient findPatientByClinicUsername(String username) {
        return patientDao.findPatientByClinicUserId(userService.getUserId(username));
    }

    public Patient findPatientByVisitId(Integer visitId) {
        return patientDao.findPatientByVisitId(visitId);
    }
}
