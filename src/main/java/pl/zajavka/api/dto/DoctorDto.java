package pl.zajavka.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import pl.zajavka.domain.Doctor;

@AllArgsConstructor
@Builder
@With
@Data
public class DoctorDto {
    private Integer id;

    private String name;

    private String surname;

    @Size(min = 11, max = 11)
    private String pesel;

    private Doctor.Specialization specialization;

}
