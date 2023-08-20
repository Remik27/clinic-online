package pl.zajavka.infrastructure.nfz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.NFZTermsDao;
import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.infrastructure.nfz.api.SownikiApi;
import pl.zajavka.infrastructure.nfz.api.TerminyLeczeniaApi;
import pl.zajavka.infrastructure.nfz.model.BenefitsResponse;
import pl.zajavka.infrastructure.nfz.model.QueuesResponse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NFZClient implements NFZTermsDao {

    private final SownikiApi sownikiApi;
    private final TerminyLeczeniaApi terminyLeczeniaApi;
    private final NFZMapper nfzMapper;
    @Override
    public List<String> getAvailableBenefits(String benefit) {
        BenefitsResponse json = sownikiApi.get(1, 25, null, benefit, "1.3").block();
        return Objects.requireNonNull(json).getData();
    }

    @Override
    public Optional<TermFromNfz> getFirstTerm(FormNfz formAttributes) {
        QueuesResponse json = terminyLeczeniaApi.get(1,
                1,
                "json",
                formAttributes.getPriorityId(),
                formAttributes.getVoivodeshipId(),
                formAttributes.getBenefit(),
                formAttributes.getForChildren(),
                "",
                "",
                "",
                formAttributes.getLocation(),
                "1.3").block();
        if (Objects.isNull(Objects.requireNonNull(json).getData()) || json.getData().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(nfzMapper.map(Objects.requireNonNull(json.getData().get(0).getAttributes())));
    }
}
