package dev.hiquality.workshops.wiremock.excersises;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class X06StubVerificationTest extends WireMockSuite {
  @Test
  public void verifyStubHit() {

    // Prepare a stub that will return a response with body containing JSON {"temperature":"14.3"}

    given().spec(requestSpec).when().get("/forecast").then().statusCode(200);
    given().spec(requestSpec).when().get("/forecast").then().statusCode(200);

    // Verification if the stub was already hit exactly two times
  }
}
