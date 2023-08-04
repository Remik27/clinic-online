package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.PatientEntity;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
public class Visit {

    private Integer id;

    private String description;

    private OffsetDateTime term;

    private String status;

    private DoctorEntity doctor;

    private PatientEntity patient;

    enum Status{
        DONE,
        FUTURE,
        CANCELLED
    }
}
