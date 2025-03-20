package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class E04TemplatingTest extends WireMockSuite {

  @Test
  public void setupStubResponseTemplatingHttpMethod() {

    stubFor(
        any(urlEqualTo("/template-http-method"))
            .willReturn(
                aResponse()
                    .withBody("You used an HTTP {{request.method}}")
                    .withTransformers("response-template")));

    given()
        .spec(requestSpec)
        .when()
        .get("/template-http-method")
        .then()
        .statusCode(200)
        .body(org.hamcrest.Matchers.equalTo("You used an HTTP GET"));
  }

  @Test
  public void setupStubResponseTemplatingJsonBody() {

    stubFor(
        post(urlEqualTo("/template-json-body"))
            .willReturn(
                aResponse()
                    .withBody("{{jsonPath request.body '$.book.title'}}")
                    .withTransformers("response-template")));

    given()
        .spec(requestSpec)
        .body("{\"book\": {\"title\": \"The Lord of the Rings\"}}")
        .when()
        .post("/template-json-body")
        .then()
        .statusCode(200)
        .body(org.hamcrest.Matchers.equalTo("The Lord of the Rings"));
  }
}
