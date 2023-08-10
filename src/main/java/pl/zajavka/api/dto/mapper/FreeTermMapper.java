package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.domain.FreeTerm;

@Mapper(componentModel = "spring")
public interface FreeTermMapper {

    default FreeTermDto mapToDto(FreeTerm term){
        return FreeTermDto.builder()
                .id(term.getId())
                .date("%d-%d-%d"
                        .formatted(term.getTerm().getYear(),
                        term.getTerm().getMonthValue(),
                        term.getTerm().getDayOfMonth()))
                .time("%d:%d".formatted(term.getTerm().getHour(), term.getTerm().getMinute()))
                .doctorName("%s %s".formatted(term.getDoctor().getName(), term.getDoctor().getSurname()))
                .build();
    }
}
