package dev.hiquality.workshops.wiremock.examples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.stubbing.Scenario;
import dev.hiquality.workshops.wiremock.common.WireMockSuite;
import org.junit.jupiter.api.Test;

public class E03StatefulBehaviourTest extends WireMockSuite {

  @Test
  public void setupStubStateful() {

    stubFor(
        get(urlEqualTo("/order"))
            .inScenario("Order processing")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse().withBody("Your shopping cart is empty")));

    stubFor(
        post(urlEqualTo("/order"))
            .inScenario("Order processing")
            .whenScenarioStateIs(Scenario.STARTED)
            .withRequestBody(equalTo("Ordering 1 item"))
            .willReturn(aResponse().withBody("Item placed in shopping cart"))
            .willSetStateTo("ORDER_PLACED"));

    stubFor(
        get(urlEqualTo("/order"))
            .inScenario("Order processing")
            .whenScenarioStateIs("ORDER_PLACED")
            .willReturn(aResponse().withBody("There is 1 item in your shopping cart")));
  }
}
