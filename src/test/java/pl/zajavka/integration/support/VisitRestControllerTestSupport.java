package pl.zajavka.integration.support;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.zajavka.api.controller.rest.VisitRestController;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.VisitsDtos;

import static org.hamcrest.Matchers.*;

public interface VisitRestControllerTestSupport {

    RequestSpecification requestSpecification();

    default Integer deleteCancelledVisits() {
        return requestSpecification()
                .when()
                .delete(VisitRestController.VISIT_API + VisitRestController.DELETE_CANCELLED_VISIT)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .and()
                .extract()
                .as(Integer.class);
    }

    default VisitsDtos getVisitsByPesel(String pesel) {
         return requestSpecification()
                 .pathParam("pesel", pesel)
                .when()
                .get(VisitRestController.VISIT_API + VisitRestController.GET_VISITS_BY_PESEL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("visits", notNullValue())
                .extract()
                .as(VisitsDtos.class);
    }

    default VisitDto cancelVisit(Integer id) {
        return requestSpecification()
                .pathParam("visitId", id)
                .when()
                .put(VisitRestController.VISIT_API + VisitRestController.CANCEL_VISIT)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("status", equalTo("CANCELLED"))
                .extract()
                .as(VisitDto.class);
    }
}
