package dev.hiquality.workshops.wiremock.answers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.stubbing.Scenario;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class A03StatefulBehaviourTest extends WireMockSuite {

  private static final String requestBody =
      """
         {"id": "LODZ_PL", latitude: 51.75, longitude: 19.4667}
      """;

  /************************************************
   * Create a stub that will respond to a GET request
   * to /location/LODZ_PL with an empty response body and
   * HTTP status code 404. However, after the POST
   * request to /location with a JSON body containing
   * a field 'id' with value 'LODZ_PL' is made,
   * the stub should change its state and
   * request, the stub should change its state and
   * respond to all subsequent requests with a response
   * body containing expected location details and
   * HTTP status code 200.
   ************************************************/
  private void setupStubExercise301() {
    stubFor(
        get(urlEqualTo("/location/LODZ_PL"))
            .inScenario("Location details")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse().withStatus(404)));

    stubFor(
        post(urlEqualTo("/location"))
            .inScenario("Location details")
            .withRequestBody(matchingJsonPath("$.id", equalTo("LODZ_PL")))
            .willReturn(aResponse().withStatus(200))
            .willSetStateTo("Location added"));

    stubFor(
        get(urlEqualTo("/location/LODZ_PL"))
            .inScenario("Location details")
            .whenScenarioStateIs("Location added")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(requestBody)));
  }

  @Test
  public void testExercise301() {
    setupStubExercise301();

    // Test that the GET request returns 404 before the POST request
    given().spec(requestSpec).when().get("/location/LODZ_PL").then().assertThat().statusCode(404);

    // Test that the POST request changes the state
    given()
        .spec(requestSpec)
        .with()
        .body(requestBody)
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(200);

    // Test that the GET request now returns 200 with the expected body
    given()
        .spec(requestSpec)
        .when()
        .get("/location/LODZ_PL")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType("application/json")
        .and()
        .body(org.hamcrest.Matchers.equalTo(requestBody));
  }
}
