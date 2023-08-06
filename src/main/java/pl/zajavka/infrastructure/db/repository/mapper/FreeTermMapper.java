package pl.zajavka.infrastructure.db.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreeTermMapper {

    FreeTermEntity mapToEntity(FreeTerm term);
}
