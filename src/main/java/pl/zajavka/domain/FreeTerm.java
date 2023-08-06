package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
public class FreeTerm {
    private Integer id;

    private OffsetDateTime term;

    private Doctor doctor;
}
