package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@With
@Data
public class TermFromNFZDto {

    private LocalDate firstDate;
    private Integer queueSize;
    private Integer averagePeriod;
    private String location;
    private String provider;
}
