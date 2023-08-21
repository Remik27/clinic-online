package pl.zajavka.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class VisitsDtos {
    private List<VisitDto> visits;
}
