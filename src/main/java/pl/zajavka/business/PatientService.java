package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.PatientDao;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.domain.exception.NotFoundException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDao patientDao;
    private final VisitService visitService;

    public DiseaseHistory showDiseaseHistory(Patient patient){
        if (Objects.isNull(patientDao.findPatient(patient))){
            throw new NotFoundException("Patient [%d] not found".formatted(patient.getId()));
        }
        List<Visit> visits = visitService.findVisitsByPatientId(patient.getId());
        Map<OffsetDateTime, String> diseasesMap = new HashMap<>();
        visits.forEach(visit -> diseasesMap.put(visit.getTerm(), visit.getDisease()));
        return DiseaseHistory.builder()
                .patient(patient)
                .diseaseHistory(diseasesMap)
                .build();
    }
}
