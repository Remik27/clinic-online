package pl.zajavka.util;

import pl.zajavka.domain.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class DomainFixtures {

    public static FreeTerm someTerm1(){
        return FreeTerm.builder()
                .doctor(Doctor.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,10,23,8,30,0,0, ZoneOffset.UTC))
                .build();
    }
    public static FreeTerm someTerm2(){
        return FreeTerm.builder()
                .doctor(Doctor.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,10,23,9,30,0,0, ZoneOffset.UTC))
                .build();
    }
    public static FreeTerm someTerm3(){
        return FreeTerm.builder()
                .doctor(Doctor.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,11,23,10,30,0,0, ZoneOffset.UTC))
                .build();
    }

    public static Doctor someDoctor1() {
        return Doctor.builder()
                .name("some")
                .surname("surname")
                .pesel("11111111111")
                .specialization(Doctor.Specialization.OKULISTA)
                .build();
    }
    public static Visit someVisit1(){
        return Visit.builder()
                .doctor(Doctor.builder().id(1).build())
                .patient(Patient.builder().id(1).build())
                .disease("katar")
                .term(OffsetDateTime
                        .of(2023,10,23,10,30,0,0, ZoneOffset.UTC))
                .status(Visit.Status.DONE)
                .build();
    }
    public static Visit someVisit2(){
        return Visit.builder()
                .doctor(Doctor.builder().id(1).build())
                .patient(Patient.builder().id(1).build())
                .disease("gorączka")
                .term(OffsetDateTime
                        .of(2023,11,23,10,30,0,0, ZoneOffset.UTC))
                .status(Visit.Status.DONE)
                .build();
    }
    public static Visit someVisit3(){
        return Visit.builder()
                .doctor(Doctor.builder().id(1).build())
                .patient(Patient.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,11,23,10,30,0,0, ZoneOffset.UTC))
                .status(Visit.Status.UPCOMING)
                .build();
    }
    public static Visit someVisit4(){
        return Visit.builder()
                .doctor(Doctor.builder().id(1).build())
                .patient(Patient.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,12,23,10,30,0,0, ZoneOffset.UTC))
                .status(Visit.Status.UPCOMING)
                .build();
    }

    public static Patient somePatient(){
        return Patient.builder()
                .name("test")
                .surname("test")
                .pesel("22222222222")
                .build();
    }

    public static VisitDescription someVisitDescription(){
        return VisitDescription.builder()
                .visitId(1)
                .description("some description")
                .build();
    }

    public static DiseaseHistory someDiseaseHistory(Map<OffsetDateTime, String> diseaseMap){
        return DiseaseHistory.builder()
                .patient(somePatient())
                .diseaseHistory(diseaseMap)
                .build();
    }



}
