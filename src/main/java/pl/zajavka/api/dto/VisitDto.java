package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;

@AllArgsConstructor
@NoArgsConstructor
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
