package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;

@AllArgsConstructor
@Builder
@With
@Data
public class VisitDto {
    private Integer id;

    private String description;

    private String date;

    private String time;

    private String disease;

    private Visit.Status status;

    private String doctorName;

    private Doctor.Specialization specialization;

    private Integer patientId;

    private String patientPesel;
}
