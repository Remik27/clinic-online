package pl.zajavka.util;

import pl.zajavka.api.dto.DiseaseHistoryDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;


public class DtoFixtures {
    public static PatientDto somePatientDto() {
        return PatientDto.builder()
                .name("test")
                .surname("test")
                .pesel("22222222222")
                .build();
    }

    public static VisitDto someVisitDto1() {
        return VisitDto.builder()
                .id(1)
                .description("description")
                .date("date")
                .time("time")
                .disease("disease")
                .status(Visit.Status.DONE)
                .doctorName("doctorName")
                .specialization(Doctor.Specialization.OKULISTA)
                .patientId(1)
                .patientPesel("11111111111")
                .build();
    }

    public static VisitDto someVisitDto2() {
        return VisitDto.builder()
                .id(2)
                .description("description")
                .date("date")
                .time("time")
                .disease("disease")
                .status(Visit.Status.DONE)
                .doctorName("doctorName")
                .specialization(Doctor.Specialization.OKULISTA)
                .patientId(1)
                .patientPesel("22222222222")
                .build();
    }

    public static DiseaseHistoryDto someDiseaseHistoryDto(){
        return DiseaseHistoryDto.builder()
                .patientDto(somePatientDto())
                .diseaseHistory(diseaseMap())
                .build();
    }

    private static LinkedHashMap<String, String> diseaseMap() {

            LinkedHashMap<String, String> diseaseMap = new LinkedHashMap<>();
            diseaseMap.put(OffsetDateTime
                            .of(2023,12,23,10,30,0,0,
                                    ZoneOffset.UTC).toString(),
                    "katar");
            diseaseMap.put(OffsetDateTime
                            .of(2023,11,23,10,30,0,0,
                                    ZoneOffset.UTC).toString(),
                    "gorączka");
            return diseaseMap;
    }
}