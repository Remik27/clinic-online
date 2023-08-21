package pl.zajavka.integration.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.zajavka.api.controller.rest.FreeTermsRestController;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.SpecializationsDtos;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.domain.Doctor;
import pl.zajavka.integration.configuration.RestAssuredIntegrationTestBase;
import pl.zajavka.util.DtoFixtures;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;


public class FreeTermsRestIT extends RestAssuredIntegrationTestBase {



    @Test
    public void fetFreeTermsShouldThrowBadRequestWhenSpecializationIsWrong() {
        requestSpecification()
                .pathParam("specialization", "someSpecialization")
                .when()
                .get(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.GET_FREE_TERMS)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void getSpecializations() {
        //given
        List<String> specializationsFromEnum = Arrays.stream(Doctor.Specialization.values()).map(Enum::name).toList();

        //when
        SpecializationsDtos specializations = requestSpecification()
                .when()
                .get(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.GET_SPECIALIZATIONS)
                .then()
                .statusCode(200)
                .body("specializations", hasSize(greaterThan(0)))
                .extract()
                .as(SpecializationsDtos.class);

        //then
        assertThat(specializations.getSpecializations()).isEqualTo(specializationsFromEnum);

    }

    @Test
    public void bookTermCorrectly() {
        // given
        PatientDto patientDto = DtoFixtures.somePatientDto();
        Integer termId = 2;

        //when
        VisitDto visit = requestSpecification()
                .pathParam("termId", termId)
                .contentType(ContentType.JSON)
                .body(patientDto)
                .when()
                .post(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.BOOK_TERM)
                .then()
                .statusCode(200)
                .and()
                .extract()
                .as(VisitDto.class);
        //then
        assertThat(visit.getId()).isNotNull();
        assertThat(visit.getPatientPesel()).isEqualTo(patientDto.getPesel());
    }
}
