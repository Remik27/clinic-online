package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Builder
@With
@Data
public class DiseaseHistoryDto {
    private PatientDto patientDto;
    private LinkedHashMap<String, String> diseaseHistory;
}
