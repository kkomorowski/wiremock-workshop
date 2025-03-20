package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.client.WireMock;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class E05ProxyTest extends WireMockSuite {

  @Test
  public void setupStubProxying() {

    WireMock.startRecording();

    stubFor(
        get(urlEqualTo("/proxy"))
            .willReturn(
                aResponse()
                    .proxiedFrom("https://httpbin.org/get")
                    .withProxyUrlPrefixToRemove("/proxy")));

    given().spec(requestSpec).get("/proxy").then().statusCode(200);

    WireMock.stopRecording();
  }
}
