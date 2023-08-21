package pl.zajavka.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class SpecializationsDtos {
    private List<String> specializations;

}
