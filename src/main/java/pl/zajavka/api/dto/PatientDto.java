package pl.zajavka.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@AllArgsConstructor
@Builder
@With
@Data
public class PatientDto {
    @Size(min = 11, max = 11)
    private String pesel;
    private String name;
    private String surname;
}
