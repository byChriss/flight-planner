package io.codelex.flightplanner.inmemory;

import io.codelex.flightplanner.FlightService;
import io.codelex.flightplanner.api.AddFlightRequest;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "in-memory")
class InMemoryFlightService implements FlightService {
    private final List<Flight> flights = new ArrayList<>();
    private AtomicInteger ID = new AtomicInteger();

    @Override
    synchronized public Flight addFlight(AddFlightRequest request) {
        if (isFlightPresent(request)) {
            throw new IllegalStateException();
        }

        Flight flight = new Flight((long) ID.incrementAndGet(),
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDeparture(),
                request.getArrival()
        );
        flights.add(flight);
        return flight;
    }

    private boolean isFlightPresent(AddFlightRequest request) {
        for (Flight flight : flights) {
            if (flight.getFrom().equals(request.getFrom())
                    && flight.getTo().equals(request.getTo())
                    && flight.getCarrier().equals(request.getCarrier())
                    && flight.getDepartureTime().equals(request.getDeparture())
                    && flight.getArrivalTime().equals(request.getArrival())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Flight> findFlights(FindFlightRequest request) {
        return flights.stream()
                .filter(flight -> isFlightPresent(request, flight))
                .collect(toList());
    }

    private boolean isFlightPresent(FindFlightRequest request, Flight flight) {
        if (request.getFrom().equals(flight.getFrom())
                && request.getTo().equals(flight.getTo())
                && request.getDeparture().equals(flight.getDepartureTime().toLocalDate())
                && request.getArrival().equals(flight.getArrivalTime().toLocalDate())) {
            return true;
        }
        return false;
    }

    @Override
    public Flight findById(Long id) {
        if (!isFlightByIdPresent(id)) {
            throw new NoSuchElementException();
        }
        return flights.stream()
                .filter(x -> x.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    public boolean isFlightByIdPresent(Long id) {
        for (Flight flight : flights) {
            if (flight.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    boolean isAnyFlightPresent() {
        if (flights.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public List<Flight> search(String from, String to) {

        return flights.stream()
                .filter(flight -> isAirportMatching(flight.getFrom(), from) || isAirportMatching(flight.getTo(), to))
                .collect(toList());
    }

    private boolean isAirportMatching(Airport airport, String search) {
        if (search != null && search.length() > 0) {
            if (airport.getCountry().toLowerCase().contains(search.toLowerCase().trim())
                    || airport.getCity().toLowerCase().contains(search.toLowerCase().trim())
                    || airport.getAirport().toLowerCase().contains(search.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    synchronized public void deleteById(Long id) {
        flights.removeIf(flight -> flight.getId().equals(id));
    }

    @Override
    public void clearAll() {
        flights.clear();
    }

    @Override
    public List<Flight> findAll() {
        return flights;
    }
}
