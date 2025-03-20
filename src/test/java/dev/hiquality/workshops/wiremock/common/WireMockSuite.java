package dev.hiquality.workshops.wiremock.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class WireMockSuite {

  public RequestSpecification requestSpec;

  public final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

  @BeforeEach
  public void setup() {
    WireMock.configureFor("localhost", 8080);
    requestSpec = new RequestSpecBuilder().setBaseUri("http://localhost").setPort(8080).build();
  }

  @AfterEach
  public void tearDown() {
    WireMock.resetToDefault();
  }
}
