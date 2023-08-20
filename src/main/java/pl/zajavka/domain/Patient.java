package pl.zajavka.domain;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "pesel")
@With
@Builder
public class Patient {

    private Integer id;

    private Integer clinicUserId;

    private String name;

    private String surname;

    private String pesel;

    private Set<Visit> visits;

}
