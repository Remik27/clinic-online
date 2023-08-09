package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Patient;
import pl.zajavka.infrastructure.db.entity.PatientEntity;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {
    Patient mapFromEntity(PatientEntity entity);

    PatientEntity mapToEntity(Patient patient);
}
