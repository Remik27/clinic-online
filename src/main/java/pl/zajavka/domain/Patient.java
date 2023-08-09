package pl.zajavka.domain;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode(of = "pesel")
@With
@Builder
@Data
public class Patient {

    private Integer id;

    private String name;

    private String surname;

    private String pesel;

    private Set<Visit> visits;
}
