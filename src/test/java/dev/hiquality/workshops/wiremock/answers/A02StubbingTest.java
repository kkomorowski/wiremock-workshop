package dev.hiquality.workshops.wiremock.answers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.http.Fault;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import dev.hiquality.workshops.wiremock.models.LocationRequest;
import dev.hiquality.workshops.wiremock.models.LocationResponse;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class A02StubbingTest extends WireMockSuite {

  private final LocationResponse locationResponse = new LocationResponse("LODZ_PL", 51.75, 19.4667);

  /************************************************
   * Create a stub that will respond to all POST
   * requests to /location
   * with HTTP status code 503
   ************************************************/
  public void setupStubExercise201() {
    stubFor(post(urlEqualTo("/location")).atPriority(10).willReturn(aResponse().withStatus(503)));
  }

  /************************************************
   * Create a stub that will respond to a POST request
   * to /location, but only if this request contains
   * a header 'speed' with value 'slow'.
   * <p>
   * Respond with status code 200, but only after a
   * fixed delay of 3000 milliseconds.
   ************************************************/

  public void setupStubExercise202() {
    stubFor(
        post(urlEqualTo("/location"))
            .withHeader("x-processing-speed", equalTo("slow"))
            .willReturn(aResponse().withStatus(200).withFixedDelay(3000)));
  }

  /************************************************
   * Create a stub that will respond to a POST request
   * to /location, but only if this request contains
   * a cookie 'session' with value 'invalid'
   * <p>
   * Respond with a Fault of type RANDOM_DATA_THEN_CLOSE
   ************************************************/
  public void setupStubExercise203() {
    stubFor(
        post(urlEqualTo("/location"))
            .withCookie("session", equalTo("invalid"))
            .willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
  }

  /************************************************
   * Create a stub that will respond to a POST request
   * to /location with status code 401,
   * but only if:
   * - the 'Authorization' header has value 'Bearer invalid', OR
   * - the 'Authorization' header is not present
   ************************************************/
  public void setupStubExercise204() {
    stubFor(
        post(urlEqualTo("/location"))
            .atPriority(1)
            .withHeader("Authorization", equalTo("Bearer invalid").or(absent()))
            .willReturn(aResponse().withStatus(401)));
  }

  /************************************************
   * Create a stub that will respond to a POST request
   * to /location with status code 200 and a JSON
   * response body equal to `locationResponse` serialized as JSON,
   * but only if the id of the location specified in the
   * request body is equal to LODZ_PL.
   ************************************************/
  public void setupStubExercise205() throws JsonProcessingException {
    stubFor(
        post(urlEqualTo("/location"))
            .withRequestBody(matchingJsonPath("$[?(@.id == 'LODZ_PL')]"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(locationResponse))));
  }

  @Test
  public void testExercise201() {
    setupStubExercise201();

    given()
        .spec(requestSpec)
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(503)
        .and()
        .statusLine(org.hamcrest.Matchers.containsString("Service Unavailable"));
  }

  @Test
  public void testExercise202() {
    setupStubExercise202();

    given()
        .spec(requestSpec)
        .and()
        .header("x-processing-speed", "slow")
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .time(org.hamcrest.Matchers.greaterThan(3000L));
  }

  @Test
  public void testExercise203() {
    setupStubExercise203();

    Assertions.assertThrows(
        ClientProtocolException.class,
        () ->
            given().spec(requestSpec).and().cookie("session", "invalid").when().post("/location"));
  }

  @Test
  public void testExercise204() {
    setupStubExercise204();

    given()
        .spec(requestSpec)
        .and()
        .auth()
        .oauth2("invalid")
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(401);

    given().spec(requestSpec).when().post("/location").then().assertThat().statusCode(401);

    given()
        .spec(requestSpec)
        .and()
        .auth()
        .oauth2("valid")
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void testExercise205() throws JsonProcessingException {

    setupStubExercise205();

    LocationRequest locationRequest = new LocationRequest("LODZ_PL", 51.75, 19.4667);

    given()
        .spec(requestSpec)
        .and()
        .body(locationRequest)
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(200)
        .body(org.hamcrest.Matchers.equalTo(objectMapper.writeValueAsString(locationResponse)));

    LocationRequest anotherLocationRequest = new LocationRequest("LONDON_UK", 51.75, 19.4667);

    given()
        .spec(requestSpec)
        .and()
        .body(anotherLocationRequest)
        .when()
        .post("/location")
        .then()
        .assertThat()
        .statusCode(404);
  }
}
