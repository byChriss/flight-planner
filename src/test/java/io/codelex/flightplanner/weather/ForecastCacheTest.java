package io.codelex.flightplanner.weather;

import io.codelex.flightplanner.api.Weather;
import io.codelex.flightplanner.weather.ForecastCache;
import io.codelex.flightplanner.weather.WeatherGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ForecastCacheTest {

    private WeatherGateway weatherGateway = Mockito.mock(WeatherGateway.class);
    private ForecastCache forecastCache = new ForecastCache(weatherGateway);
    private Weather weather = Mockito.mock(Weather.class);
    private LocalDate date = LocalDate.of(2019, 10, 2);

    @Test
    void should_get_weather_if_cache_is_empty() {
        //given
        when(weatherGateway.fetchForecast("Riga", date))
                .thenReturn(Optional.of(weather));
        //when
        Optional<Weather> forecast = forecastCache.fetchForecast("Riga", date);
        //then
        assertEquals(Optional.of(this.weather), forecast);
    }

    @Test
    void should_get_weather_if_there_is_already_value() {
        //given
        when(weatherGateway.fetchForecast("Riga", date))
                .thenReturn(Optional.of(weather));
        //when
        forecastCache.fetchForecast("Riga", date);
        forecastCache.fetchForecast("Riga", date);
        //then
        verify(weatherGateway, times(1)).fetchForecast("Riga", date);
    }
}