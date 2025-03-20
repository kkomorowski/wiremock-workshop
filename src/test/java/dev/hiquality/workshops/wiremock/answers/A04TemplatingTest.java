package dev.hiquality.workshops.wiremock.answers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class A04TemplatingTest extends WireMockSuite {

  /*************************************
   * Exercise 4: Response Templating
   * 1. Create a stub for POST /location that returns
   *    a JSON response with the same id, latitude
   *    and longitude as in the request body.
   * 2. Use WireMock's response templating to achieve this.
   * 3. Test the stub by sending a POST request with
   *    a JSON body containing id, latitude and longitude,
   *    and verify that the response contains the same values.
   *************************************/

  public void setupStubExercise401() {
    stubFor(
        post(urlEqualTo("/location"))
            .willReturn(
                aResponse()
                    .withBody(
                        "{\"id\":\"{{jsonPath request.body '$.id'}}\","
                            + "\"latitude\":\"{{jsonPath request.body '$.latitude'}}\","
                            + "\"longitude\":\"{{jsonPath request.body '$.longitude'}}\"}")
                    .withTransformers("response-template")));
  }

  @Test
  public void testExercise401() {
    setupStubExercise401();

    given()
        .spec(requestSpec)
        .body("{\"id\": \"LODZ_PL\", \"latitude\": \"51.75\", \"longitude\": \"19.4667\"}")
        .when()
        .post("/location")
        .then()
        .statusCode(200)
        .body(
            org.hamcrest.Matchers.equalTo(
                "{\"id\":\"LODZ_PL\",\"latitude\":\"51.75\",\"longitude\":\"19.4667\"}"));
  }
}
