package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8081)
public class E01HelloWireMockTest {

  public void setupHelloWorld() {
    stubFor(
        get(urlEqualTo("/hello"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "text/plain")
                    .withStatus(200)
                    .withBody("Hello, WireMock!")));
  }

  @Test
  public void helloWorldTest() {
    setupHelloWorld();
    given()
        .baseUri("http://localhost:8081")
        .when()
        .get("/hello")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body(equalTo("Hello, WireMock!"));
  }
}
