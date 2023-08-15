package pl.zajavka.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class FreeTermDto {
    private Integer id;

    private String date;

    private String time;

    private String doctorName;
}
