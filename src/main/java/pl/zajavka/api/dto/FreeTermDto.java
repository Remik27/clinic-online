package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@AllArgsConstructor
@Builder
@With
@Data
public class FreeTermDto {
    private Integer id;

    private String date;

    private String time;

    private String doctorName;
}
