package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
public class Visit {

    private Integer id;

    private String description;

    private OffsetDateTime term;

    private String status;

    private Doctor doctor;

    private Patient patient;

    enum Status{
        DONE,
        FUTURE,
        CANCELLED
    }
}
