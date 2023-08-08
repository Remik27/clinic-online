package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Set;

@AllArgsConstructor
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
