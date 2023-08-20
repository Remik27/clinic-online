package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDate;


@Builder
@With
@Data
@AllArgsConstructor
public class TermFromNfz {
    private LocalDate firstDate;
    private Integer queueSize;
    private Integer averagePeriod;
    private String location;
    private String provider;
}

