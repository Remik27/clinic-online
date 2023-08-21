package pl.zajavka.integration.rest;

import org.junit.jupiter.api.Test;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.VisitsDtos;
import pl.zajavka.domain.Visit;
import pl.zajavka.integration.configuration.RestAssuredIntegrationTestBase;
import pl.zajavka.integration.support.VisitRestControllerTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitRestIT extends RestAssuredIntegrationTestBase implements VisitRestControllerTestSupport {

    @Test
    public void visitRestControllerWorkCorrectly() {
        //given
        String pesel = "12345678412";
        //when
        VisitsDtos visitsDtos = getVisitsByPesel(pesel);
        VisitDto visitDto = visitsDtos.getVisits().get(0);
        VisitDto cancelledVisit = cancelVisit(visitDto.getId());
        Integer deletedElements = deleteCancelledVisits();

        //then

        assertThat(visitDto.getPatientPesel()).isEqualTo(pesel);
        assertThat(visitDto.getStatus()).isEqualTo(Visit.Status.UPCOMING);

        assertThat(cancelledVisit.getStatus()).isEqualTo(Visit.Status.CANCELLED);
        assertThat(deletedElements).isEqualTo(2);
    }




}
