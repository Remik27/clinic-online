package pl.zajavka.domain;


import lombok.*;

import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode(of = "pesel")
@With
@Builder
@Data
public class Doctor {

    private Integer id;

    private String name;

    private String surname;

    private String pesel;

    private String specialization;

    private Set<FreeTerm> freeTerms;

    private Set<Visit> visits;
}
