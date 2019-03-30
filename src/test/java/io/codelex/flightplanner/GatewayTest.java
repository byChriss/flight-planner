package io.codelex.flightplanner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.codelex.flightplanner.api.Weather;
import io.codelex.flightplanner.weather.ApixuProperties;
import io.codelex.flightplanner.weather.WeatherGateway;

import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GatewayTest {

    @Rule
    static WireMockRule wireMockRule = new WireMockRule();
    WeatherGateway gateway;
    LocalDate date = LocalDate.of(2019, 03, 03);

    @BeforeAll
    static void setupOnce() {
        wireMockRule.start();
    }

    @BeforeEach
    void setUp() {
        wireMockRule.start();
        ApixuProperties props = new ApixuProperties();
        props.setApiUrl("http://localhost:" + wireMockRule.port());
        props.setApiKey("123");
        gateway = new WeatherGateway(props);
    }

    @Test
    void should_fetch_forecast() throws Exception {
        //given
        File file = ResourceUtils.getFile(this.getClass().getResource("/stubs/successful-response.json"));
        Assertions.assertTrue(file.exists());

        byte[] json = Files.readAllBytes(file.toPath());

        wireMockRule.stubFor(get(WireMock.urlPathEqualTo("/v1/forecast.json"))
                .withQueryParam("key", equalTo("123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(200)
                        .withBody(json)));
        //when
        Weather weather = gateway.fetchForecast("Riga", date).get();
        //then
        Assertions.assertEquals("Moderate or heavy rain shower", weather.getCondition());
        Assertions.assertEquals(4.6, weather.getAverageTemperature());
        Assertions.assertEquals(23.8, weather.getMaxWind());
        Assertions.assertEquals(1.7, weather.getPrecipitation());
    }


    @Test
    void should_handle_external__service_error() {
        //given
        wireMockRule.stubFor(get(WireMock.urlPathEqualTo("/v1/forecast.json"))
                .withQueryParam("key", equalTo("123"))
                .willReturn(aResponse()
                        .withStatus(500)));
        //when
        Optional<Weather> res = gateway.fetchForecast("Riga", date);
        //then 
        assertFalse(res.isPresent());
    }
}
