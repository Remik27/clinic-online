package pl.zajavka.util;

import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
                        .of(2023,10,23,10,30,0,0, ZoneOffset.UTC))
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


}
