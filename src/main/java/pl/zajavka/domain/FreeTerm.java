package pl.zajavka.domain;

import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class FreeTerm {
    private Integer id;

    private OffsetDateTime term;

    private Doctor doctor;
}
