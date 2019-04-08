package io.codelex.flightplanner.weather;


import io.codelex.flightplanner.FlightDecorator;
import io.codelex.flightplanner.api.*;

import io.codelex.flightplanner.repository.RepositoryFlightService;
import io.codelex.flightplanner.weather.ForecastCache;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FlightDecoratorTest {
    private RepositoryFlightService flightService = Mockito.mock(RepositoryFlightService.class);
    private ForecastCache cache = Mockito.mock(ForecastCache.class);

    FlightDecorator decorator = new FlightDecorator(
            flightService,
            cache
    );
    LocalDate defaultDate = LocalDate.of(2019, 1, 1);
    Weather defaultWeather = new Weather(1.0, 1.0, 1.0, "Snow");

    @Test
    public void should_combine_result_from_service_and_gateway() {
        //given
        FindFlightRequest request = new FindFlightRequest(
                new Airport("LV", "Riga", "RIX"),
                new Airport("SWE", "Stockholm", "ARN"),
                defaultDate,
                defaultDate.plusDays(1)
        );
        List<Flight> flightsFromService = Arrays.asList(
                new Flight(
                        1L,
                        new Airport("LV", "Riga", "RIX"),
                        new Airport("SWE", "Stockholm", "ARN"),
                        "Ryanair",
                        defaultDate.atStartOfDay(),
                        defaultDate.plusDays(1).atStartOfDay()
                )
        );
        Mockito.when(flightService.findFlights(request))
                .thenReturn(flightsFromService);
        Mockito.when(cache.fetchForecast("Stockholm", defaultDate.plusDays(1)))
                .thenReturn(Optional.of(defaultWeather));
        //when
        List<FlightWithWeather> flight = decorator.findFlight(request);
        //then
        Assertions.assertEquals(1, flight.size());
        //when
        FlightWithWeather singleFlight = flight.get(0);
        //then
        Assertions.assertEquals(
                "Snow",
                singleFlight.getWeather().getCondition());

    }
}
