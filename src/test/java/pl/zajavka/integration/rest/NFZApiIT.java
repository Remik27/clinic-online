package pl.zajavka.integration.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.business.NFZService;
import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.infrastructure.nfz.NFZClient;
import pl.zajavka.integration.configuration.RestAssuredIntegrationTestBase;
import pl.zajavka.integration.support.WiremockTestSupport;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NFZApiIT extends RestAssuredIntegrationTestBase implements WiremockTestSupport {

    private final NFZClient nfzClient;

    @Test
    void thatFindingFirstFreeTermCorrectly(){
        //given
        String benefit = "okulista";
        stubForSlowniki(wireMockServer, benefit);
        stubForTerminyLeczenia(wireMockServer);

        //when
        List<String> availableBenefits = nfzClient.getAvailableBenefits(benefit);
        Optional<TermFromNfz> firstTerm = nfzClient.getFirstTerm(buildFormNfz());

        //then
       assertThat(firstTerm.isPresent()).isTrue();
       assertThat(availableBenefits.size()).isGreaterThan(0);

    }

    private FormNfz buildFormNfz() {
        return FormNfz.builder()
                .forChildren(false)
                .priorityId(1)
                .voivodeshipId("01")
                .location("Wrocław")
                .benefit("ODDZIAŁ OKULISTYCZNY")
                .build();
    }


}
