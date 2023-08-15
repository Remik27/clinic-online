package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.DoctorDto;
import pl.zajavka.domain.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto mapToDto(Doctor doctor);
}
