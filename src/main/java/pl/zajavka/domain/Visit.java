package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.OffsetDateTime;

@AllArgsConstructor
@With
@Builder
@Data
public class Visit {

    private Integer id;

    private String description;

    private OffsetDateTime term;

    private Status status;

    private Doctor doctor;

    private Patient patient;

    public enum Status{
        DONE,
        FUTURE,
        CANCELLED
    }
}
