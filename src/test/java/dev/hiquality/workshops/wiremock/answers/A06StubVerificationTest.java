package dev.hiquality.workshops.wiremock.answers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.client.WireMock;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class A06StubVerificationTest extends WireMockSuite {
  @Test
  public void verifyStubHit() {

    // Prepare a stub that will return a response with body containing JSON {"temperature":"14.3"}
    stubFor(
        get(urlEqualTo("/forecast"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"temperature\":\"14.3\"}")));

    given().spec(requestSpec).when().get("/forecast").then().statusCode(200);
    given().spec(requestSpec).when().get("/forecast").then().statusCode(200);

    // Verification if the stub was already hit exactly two times
    verify(exactly(2), WireMock.getRequestedFor(urlEqualTo("/forecast")));
  }
}
