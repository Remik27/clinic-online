package pl.zajavka.api.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class PatientDto {
    @Size(min = 11, max = 11, message = "pesel")
    private String pesel;
    private String name;
    private String surname;
}
