package pl.zajavka.util;

import pl.zajavka.domain.Doctor;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.infrastructure.db.entity.VisitEntity;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.Roles;
import pl.zajavka.infrastructure.security.UserDto;
import pl.zajavka.infrastructure.security.UserEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

public class EntityFixtures {

    public static FreeTermEntity someTerm1(){
        return FreeTermEntity.builder()
                .doctor(DoctorEntity.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,10,23,8,30,0,0, ZoneOffset.UTC))
                .build();
    }
    public static FreeTermEntity someTerm2(){
        return FreeTermEntity.builder()
                .doctor(DoctorEntity.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,10,23,9,30,0,0, ZoneOffset.UTC))
                .build();
    }
    public static FreeTermEntity someTerm3(){
        return FreeTermEntity.builder()
                .doctor(DoctorEntity.builder().id(1).build())
                .term(OffsetDateTime
                        .of(2023,11,23,10,30,0,0, ZoneOffset.UTC))
                .build();
    }

    public static DoctorEntity someDoctor1() {
        return DoctorEntity.builder()
                .name("some")
                .surname("surname")
                .pesel("11111111111")
                .specialization("okulista")
                .build();
    }
    public static VisitEntity someVisit1(){
        return VisitEntity.builder()
                .doctor(DoctorEntity.builder().id(1).build())
                .patient(PatientEntity.builder().id(1).build())
                .disease("katar")
                .term(OffsetDateTime
                        .of(2023,10,23,10,30,0,0, ZoneOffset.UTC))
                .status("DONE")
                .build();
    }
    public static VisitEntity someVisit2(){
        return VisitEntity.builder()
                .doctor(DoctorEntity.builder().id(1).build())
                .patient(PatientEntity.builder().id(1).build())
                .disease("gorączka")
                .term(OffsetDateTime
                        .of(2023,11,23,10,30,0,0, ZoneOffset.UTC))
                .status("DONE")
                .build();
    }

    public static PatientEntity somePatient(){
        return PatientEntity.builder()
                .name("test")
                .surname("test")
                .pesel("22222222222")
                .build();
    }


    public static RoleEntity someRole() {
        return RoleEntity.builder()
                .id(1)
                .role(Roles.DOCTOR.name())
                .build();
    }

    public static UserEntity someUser() {
        return UserEntity.builder()
                .userName("username")
                .password("password")
                .email("email@email.pl")
                .roles(Set.of(someRole()))
                .active(true)
                .build();
    }
}
