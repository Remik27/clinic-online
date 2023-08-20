package pl.zajavka.api.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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
