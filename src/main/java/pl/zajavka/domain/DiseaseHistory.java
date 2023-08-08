package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class DiseaseHistory {

    private Patient patient;
    private Map<OffsetDateTime, String> diseaseHistory;
}
