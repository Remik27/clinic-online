package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.domain.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "id", ignore = true)
    Patient mapFromDto(PatientDto patientDto);


    PatientDto mapToDto(Patient patient);
}
