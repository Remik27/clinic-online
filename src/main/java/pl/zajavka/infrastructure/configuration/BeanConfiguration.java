package pl.zajavka.infrastructure.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;

@Configuration
public class BeanConfiguration {
    private final static String DATA_FORMAT = "yyyy-MM-dd";
//    static class OffsetDeserializer extends InstantDeserializer<OffsetDateTime> {

//        public OffsetDeserializer() {
//            super(OffsetDateTime.class, DateTimeFormatter.ISO_DATE_TIME, (temporal) -> {
//                        if (temporal.query(TemporalQueries.offset()) == null) {
//                            return LocalDateTime.from(temporal).atOffset(ZoneOffset.UTC);
//                        } else {
//                            return OffsetDateTime.from(temporal);
//                        }
//                    },
//                    (a) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId),
//                    (a) -> OffsetDateTime.ofInstant(Instant.ofEpochSecond(a.integer, (long)a.fraction), a.zoneId),
//                    (d, z) -> d.withOffsetSameInstant(z.getRules().getOffset(d.toLocalDateTime())), true);
//        }
//    }

    @Bean
    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())

                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setDateFormat(new SimpleDateFormat(DATA_FORMAT))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

