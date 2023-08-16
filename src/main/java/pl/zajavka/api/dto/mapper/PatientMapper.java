package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.zajavka.api.dto.DiseaseHistoryDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface PatientMapper extends OffsetDateTimeMapper {
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "id", ignore = true)
    Patient mapFromDto(PatientDto patientDto);


    PatientDto mapToDto(Patient patient);
@Mapping(target = "patientDto", source = "patient")
    @Mapping(target = "diseaseHistory", source = "diseaseHistory", qualifiedByName = "mapDiseaseMap")
    DiseaseHistoryDto mapDiseaseHistory(DiseaseHistory diseaseHistory);

    @Named("mapDiseaseMap")
    default LinkedHashMap<String, String> mapDiseaseMap(Map<OffsetDateTime, String> diseaseHistory){
        LinkedHashMap<String, String> diseaseHistoryDto = new LinkedHashMap<>();
        for (Map.Entry<OffsetDateTime, String> diseaseEntry : diseaseHistory.entrySet()) {
            diseaseHistoryDto.put(mapOffsetDateTimeToString(diseaseEntry.getKey()), diseaseEntry.getValue());
        }
        return diseaseHistoryDto;

    }
}
