package pl.zajavka.infrastructure.nfz;

import org.mapstruct.Mapper;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.infrastructure.nfz.model.QueueAttributes;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NFZMapper {
    default TermFromNfz map(QueueAttributes queue){
        return TermFromNfz.builder()
                .firstDate(Objects.requireNonNull
                        (Objects.requireNonNull
                                (Objects.requireNonNull
                                        (queue.getDates()).getDate())))
                .averagePeriod(Objects.requireNonNull
                        (Objects.requireNonNull
                                (queue.getStatistics()).getProviderData()).getAveragePeriod())
                .queueSize(queue.getStatistics().getProviderData().getAwaiting())
                .location(queue.getAddress())
                .provider(queue.getProvider())
                .build();
    }
}
