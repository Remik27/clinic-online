package pl.zajavka.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.entity.VisitEntity;

import java.util.Set;

@AllArgsConstructor
@Builder
@Data
public class Doctor {

    private Integer id;

    private String name;

    private String surname;

    private String pesel;

    private Set<FreeTermEntity> freeTerms;

    private Set<VisitEntity> visits;
}
