package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class SpecializationsDtos {
    private List<String> specializations;

}
