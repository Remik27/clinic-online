package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.domain.Visit;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    @Mapping(target = "doctorName", expression = "java(visit.getDoctor().getName() + \" \" + visit.getDoctor().getSurname())")
    @Mapping(target = "patientId", source = "visit.patient.id")
    @Mapping(target = "patientPesel", source = "visit.patient.pesel")
    @Mapping(target = "specialization", source = "visit.doctor.specialization")
    @Mapping(target = "date", expression = "java(visit.getTerm().toLocalDate().toString())")
    @Mapping(target = "time", expression = "java(visit.getTerm().toLocalTime().toString())")
    VisitDto mapToDto(Visit visit);
}
