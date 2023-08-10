package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Patient;
import pl.zajavka.infrastructure.db.entity.PatientEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientEntityMapper {
    Patient mapFromEntity(PatientEntity entity);

    PatientEntity mapToEntity(Patient patient);
}
