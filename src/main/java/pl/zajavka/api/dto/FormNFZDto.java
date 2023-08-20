package pl.zajavka.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class FormNFZDto {

    private String benefit;
    private String voivodeship;
    private String location;
    private String priority;
    private Boolean forChildren;
}
