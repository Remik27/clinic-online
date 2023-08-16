package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import pl.zajavka.api.dto.FreeTermDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface OffsetDateTimeMapper {

    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Named("mapOffsetDateTimeToString")
    default String mapOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
            .map(odt -> offsetDateTime.atZoneSameInstant(ZoneOffset.UTC))
            .map(odt -> odt.format(DATE_FORMAT))
            .orElse(null);
    }

    @Named("mapOffsetDateTimeFromString")
    default OffsetDateTime mapOffsetDateTimeFromString(FreeTermDto freeTermDto) {
        String dateTime = freeTermDto.getDate() + " " + freeTermDto.getTime();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DATE_FORMAT);
        return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}
