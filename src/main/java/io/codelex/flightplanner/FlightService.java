package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;

import java.util.List;

public interface FlightService {
    Flight addFlight(AddFlightRequest request);

    List<Flight> findFlights(FindFlightRequest request);

    Flight findById(Long id);

    List<Flight> search(String from, String to);

    void deleteById(Long id);

    void clearAll();

    List<Flight> findAll();


}
