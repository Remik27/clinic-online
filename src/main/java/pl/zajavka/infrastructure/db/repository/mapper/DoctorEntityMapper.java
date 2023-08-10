package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Doctor;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorEntityMapper {


    DoctorEntity mapToEntity(Doctor doctor);

    Doctor mapFromEntity(DoctorEntity entity);
}