package pl.zajavka.domain;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@With
@Builder
public class Visit {

    private Integer id;

    private String description;

    private OffsetDateTime term;

    private String disease;

    private Status status;

    private Doctor doctor;

    private Patient patient;

    protected boolean canEqual(final Object other) {
        return other instanceof Visit;
    }

    public enum Status{
        DONE,
        FUTURE,
        CANCELLED
    }
}
