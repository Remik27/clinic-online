package pl.zajavka.domain;


import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "pesel")
@With
@Builder
@Data
public class Doctor {

    private Integer id;

    private Integer clinicUserId;

    private String name;

    private String surname;

    private String pesel;

    private Specialization specialization;

    private Set<FreeTerm> freeTerms;

    private Set<Visit> visits;

    public enum Specialization{
        OKULISTA,
        KARDIOLOG,
        UROLOG

    }
}
