package pl.zajavka.util;

import pl.zajavka.api.dto.DiseaseHistoryDto;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.Roles;
import pl.zajavka.infrastructure.security.UserDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;


public class DtoFixtures {
    public static PatientDto somePatientDto() {
        return PatientDto.builder()
                .name("test")
                .surname("test")
                .pesel("11111111111")
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

    public static DiseaseHistoryDto someDiseaseHistoryDto() {
        return DiseaseHistoryDto.builder()
                .patientDto(somePatientDto())
                .diseaseHistory(diseaseMap())
                .build();
    }

    private static LinkedHashMap<String, String> diseaseMap() {

        LinkedHashMap<String, String> diseaseMap = new LinkedHashMap<>();
        diseaseMap.put(OffsetDateTime
                        .of(2023, 12, 23, 10, 30, 0, 0,
                                ZoneOffset.UTC).toString(),
                "katar");
        diseaseMap.put(OffsetDateTime
                        .of(2023, 11, 23, 10, 30, 0, 0,
                                ZoneOffset.UTC).toString(),
                "gorączka");
        return diseaseMap;
    }

    public static FreeTermDto someTermDto1() {
        return FreeTermDto.builder()
                .doctorName("doctorName")
                .date(LocalDate.of(2023, 10, 22))
                .time(LocalTime.of(9, 30).toString())
                .build();
    }

    public static FreeTermDto someTermDto2() {
        return FreeTermDto.builder()
                .doctorName("doctorName")
                .date(LocalDate.of(2023, 10, 23))
                .time(LocalTime.of(9, 30).toString())
                .build();
    }

    public static UserDto someUserDto() {
        return UserDto.builder()
                .username("username")
                .password("password")
                .email("email@email.pl")
                .name("name")
                .surname("surname")
                .pesel("11111111111")
                .role(Roles.DOCTOR)
                .specialization(Doctor.Specialization.OKULISTA)
                .build();
    }
}