package dev.hiquality.workshops.wiremock.excersises;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class X05ProxyTest extends WireMockSuite {

  @Test
  public void setupStubProxying() {

    // Start stub recording on the WireMock

    // Prepare the stub which for all requests starting with /forecast
    // will proxy to https://api.open-meteo.com/v1/

    given()
        .spec(requestSpec)
        .queryParam("latitude", 51.75)
        .queryParam("longitude", 19.46)
        .queryParam("current", "temperature_2m")
        .get("/forecast")
        .then()
        .statusCode(200);

    // Stop recording
  }
}
