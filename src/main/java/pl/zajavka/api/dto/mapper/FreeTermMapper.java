package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.domain.FreeTerm;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface FreeTermMapper {

    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    @Mapping(target = "term", source = "freeTermDto", qualifiedByName = "mapOffsetDateTimeFromString")
    @Mapping(target = "doctor", ignore = true)
    FreeTerm mapFromDto(FreeTermDto freeTermDto);

    @Named("mapOffsetDateTimeFromString")
    default OffsetDateTime mapOffsetDateTimeFromString(FreeTermDto freeTermDto) {
        String dateTime = freeTermDto.getDate() + " " + freeTermDto.getTime();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DATE_FORMAT);
        return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}
