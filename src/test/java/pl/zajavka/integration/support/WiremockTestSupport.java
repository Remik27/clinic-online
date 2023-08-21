package pl.zajavka.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public interface WiremockTestSupport {

    default void stubForTerminyLeczenia(WireMockServer wireMockServer) {
        wireMockServer.stubFor(
                get(urlPathMatching("/queues"))
                        .withQueryParam("benefit", equalTo("ODDZIAŁ OKULISTYCZNY"))
                        .withQueryParam("province", equalTo("01"))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("wiremock/first-term.json")));
    }

    default void stubForSlowniki(WireMockServer wireMockServer, String benefit) {
        wireMockServer.stubFor(
                get(urlPathMatching("/benefits"))
                        .withQueryParam("name", equalTo(benefit))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("wiremock/slowniki-benefits.json")));
    }
}
