package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Component
class FlightService {
    private final List<Flight> flights = new ArrayList<>();
    private AtomicInteger ID = new AtomicInteger();

    synchronized Flight addFlight(AddFlightRequest request) {
        if (isFlightPresent(request)) {
            throw new IllegalStateException();
        }
        if (addRequestContainsNulls(request) || addContainsStrangeTime(request)) {
            throw new NullPointerException();
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

    private boolean addRequestContainsNulls(AddFlightRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            return true;
        }
        if (request.getDeparture() == null || request.getArrival() == null) {
            return true;
        }
        if (request.getCarrier() == null || request.getCarrier().equals("")) {
            return true;
        }
        if (isHavingNullFields(request.getFrom())) {
            return true;
        }
        if (isHavingNullFields(request.getTo())) {
            return true;
        }
        if (isHavingEmptyString(request.getFrom())) {
            return true;
        }
        if (isHavingEmptyString(request.getTo())) {
            return true;
        }
        if (request.getFrom().equals(request.getTo())) {
            return true;
        }
        if (request.getFrom().getAirport().toLowerCase().trim().equals(request.getTo().getAirport().toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    private boolean addContainsStrangeTime(AddFlightRequest request) {
        if (request.getArrival().isBefore(request.getDeparture())
                || request.getArrival().isEqual(request.getDeparture())) {
            return true;
        }
        return false;
    }

    List<Flight> findFlights(FindFlightRequest request) {
        if (findRequestContainsNulls(request)) {
            throw new NullPointerException();
        }
        return flights.stream()
                .filter(flight -> isFlightPresent(request, flight))
                .collect(toList());
    }

    private boolean findRequestContainsNulls(FindFlightRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            return true;
        }
        if (request.getDeparture() == null || request.getArrival() == null) {
            return true;
        }
        if (isHavingNullFields(request.getFrom())) {
            return true;
        }
        if (isHavingNullFields(request.getTo())) {
            return true;
        }
        if (isHavingEmptyString(request.getFrom())) {
            return true;
        }
        if (isHavingEmptyString(request.getTo())) {
            return true;
        }
        if (request.getFrom().equals(request.getTo())) {
            return true;
        }
        if (request.getFrom().getAirport().toLowerCase().trim().equals(request.getTo().getAirport().toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    private boolean isHavingEmptyString(Airport from) {
        return from.getCountry().equals("")
                && from.getCity().equals("")
                && from.getAirport().equals("");
    }

    private boolean isHavingNullFields(Airport from) {
        return from.getCountry() == null
                && from.getCity() == null
                && from.getAirport() == null;
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

    Flight findById(Long id) {
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


    List<Flight> search(String from, String to) {

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

    synchronized void deleteById(Long id) {
        flights.removeIf(flight -> flight.getId().equals(id));
    }

    void clearAll() {
        flights.clear();
    }

    List<Flight> findAll() {
        return flights;
    }
}
