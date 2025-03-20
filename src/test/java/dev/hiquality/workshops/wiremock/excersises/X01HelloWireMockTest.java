package dev.hiquality.workshops.wiremock.excersises;

import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8081)
class X01HelloWireMockTest {

  private RequestSpecification requestSpec;

  @BeforeEach
  public void createRequestSpec() {
    requestSpec = new RequestSpecBuilder().setBaseUri("http://localhost").setPort(8081).build();
  }

  /************************************************
   * Create a stub that will respond to a GET
   * to /hello with an HTTP status code 200
   ************************************************/
  public void setupStubExercise101() {}

  /************************************************
   * Create a stub that will respond to a GET
   * to /hello with a response that contains
   * a Content-Type header with value application/json
   ************************************************/
  public void setupStubExercise102() {}

  /************************************************
   * Create a stub that will respond to a GET
   * to /hello with a plain text response body
   * equal to 'Hello, WireMock!'
   ************************************************/
  public void setupStubExercise103() {}

  @Test
  public void testExercise101() {
    setupStubExercise101();
    given().spec(requestSpec).when().get("/hello").then().assertThat().statusCode(200);
  }

  @Test
  public void testExercise102() {
    setupStubExercise102();
    given()
        .spec(requestSpec)
        .when()
        .get("/hello")
        .then()
        .assertThat()
        .contentType(ContentType.JSON);
  }

  @Test
  public void testExercise103() {
    setupStubExercise103();
    given()
        .spec(requestSpec)
        .when()
        .get("/hello")
        .then()
        .assertThat()
        .body(org.hamcrest.Matchers.equalTo("Hello, WireMock!"));
  }
}
