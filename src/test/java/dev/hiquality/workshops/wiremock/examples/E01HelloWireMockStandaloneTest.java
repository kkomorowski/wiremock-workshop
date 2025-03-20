package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class E01HelloWireMockStandaloneTest {

  @BeforeEach
  public void setupWireMock() {
    WireMock.configureFor("localhost", 8080);
    WireMock.removeAllMappings();
  }

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
        .baseUri("http://localhost:8080")
        .when()
        .get("/hello")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body(equalTo("Hello, WireMock!"));
  }
}
