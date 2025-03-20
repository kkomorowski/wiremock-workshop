package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.client.WireMock;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class E06StubVerificationTest extends WireMockSuite {
  @Test
  public void verifyStubHit() {
    stubFor(
        get(urlEqualTo("/forecast"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"temperature\":\"14.3\"}")));

    given().spec(requestSpec).when().get("/forecast").then().statusCode(200);

    // Verification if the stub was already hit at least once
    verify(moreThanOrExactly(1), WireMock.getRequestedFor(urlEqualTo("/forecast")));
  }
}
