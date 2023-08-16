package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.TreeMap;

@AllArgsConstructor
@Builder
@Data
public class DiseaseHistory {

    private Patient patient;
    private TreeMap<OffsetDateTime, String> diseaseHistory;
}
