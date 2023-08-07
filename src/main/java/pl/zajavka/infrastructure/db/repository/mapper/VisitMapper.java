package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.db.entity.VisitEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    VisitEntity mapToEntity(Visit visit);

    Visit mapFromEntity(VisitEntity entity);
}
