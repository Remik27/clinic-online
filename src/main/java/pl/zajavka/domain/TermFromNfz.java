package pl.zajavka.domain;

import lombok.*;

import java.time.LocalDate;


@Builder
@With
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermFromNfz {
    private LocalDate firstDate;
    private Integer queueSize;
    private Integer averagePeriod;
    private String location;
    private String provider;
}

