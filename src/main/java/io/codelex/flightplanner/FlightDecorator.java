package io.codelex.flightplanner;

import io.codelex.flightplanner.api.*;

import io.codelex.flightplanner.weather.ForecastCache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class FlightDecorator {

    private FlightService service;
    private ForecastCache cache;

    public FlightDecorator(FlightService service, ForecastCache cache) {
        this.service = service;
        this.cache = cache;
    }

    public List<FlightWithWeather> findFlight(FindFlightRequest request) {
        List<Flight> flightList = service.findFlights(request);
        List<FlightWithWeather> result = new ArrayList<>();

        for (Flight flight : flightList) {
            result.add(decorate(flight));
        }
        return result;
    }

    private FlightWithWeather decorate(Flight flight) {
        Airport to = flight.getTo();
        Optional<Weather> weather = cache.fetchForecast(flight.getTo().getCity(),
                flight.getArrivalTime().toLocalDate());
        return new FlightWithWeather(
                flight.getId(),
                flight.getFrom(),
                to,
                flight.getCarrier(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                weather.orElse(null)
        );
    }

}
