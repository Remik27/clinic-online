package pl.zajavka.domain;

import lombok.*;

@AllArgsConstructor
@With
@Builder
@Data
public class FormNfz {

    private String benefit;
    private String voivodeshipId;
    private String location;
    private Integer priorityId;
    private Boolean forChildren;
}
