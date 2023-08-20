package pl.zajavka.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class VoivodeshipsDto {
    private List<String> voivodeships;
}
