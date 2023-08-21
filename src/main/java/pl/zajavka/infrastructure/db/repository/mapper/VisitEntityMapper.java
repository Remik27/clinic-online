package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.infrastructure.db.entity.VisitEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitEntityMapper {
    VisitEntity mapToEntity(Visit visit);

    @Mapping(target = "patient", source = "patient", qualifiedByName = "mapFromPatientEntity")
    @Mapping(target = "doctor.visits",ignore = true )
    @Mapping(target = "doctor.freeTerms", ignore = true)
    Visit mapFromEntity(VisitEntity entity);

    @Named("mapFromPatientEntity")
    @Mapping(target = "visits", ignore = true)
    Patient mapFromPatientEntity(PatientEntity entity);
}
