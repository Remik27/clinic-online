package pl.zajavka.api.dto;

import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class FreeTermDto {
    private Integer id;
    @Future
    private LocalDate date;

    private String time;

    private String doctorName;
}
