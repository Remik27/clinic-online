package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import pl.zajavka.api.dto.FreeTermDto;

import java.util.List;

@AllArgsConstructor
@Builder
@With
@Data
public class FreeTermDtos {
    private List<FreeTermDto> freeTermDtos;
}
