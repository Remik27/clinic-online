package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreeTermEntityMapper {

    FreeTermEntity mapToEntity(FreeTerm term);

@Mapping(target = "doctor.freeTerms", ignore = true)
@Mapping(target = "doctor.visits", ignore = true)
    FreeTerm mapFromEntity(FreeTermEntity entity);
}
