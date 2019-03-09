package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.FlightService;
import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public class RepositoryFlightService implements FlightService {
    private final FlightRecordRepository flightRecordRepository;
    private final AirportRecordRepository airportRecordRepository;
    private final MapFlightRecordToFlight toFlight = new MapFlightRecordToFlight();

    public RepositoryFlightService(FlightRecordRepository flightRecordRepository, AirportRecordRepository airportRecordRepository) {
        this.flightRecordRepository = flightRecordRepository;
        this.airportRecordRepository = airportRecordRepository;
    }

    @Override
    public Flight addFlight(AddFlightRequest request) {
        if (flightRecordRepository.isFlightPresent(
                request.getFrom().getAirport(),
                request.getTo().getAirport(),
                request.getDeparture(),
                request.getArrival(),
                request.getCarrier())) {
            throw new IllegalStateException();
        }
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFrom(createOrGetAirport(request.getFrom()));
        flightRecord.setTo(createOrGetAirport(request.getTo()));
        flightRecord.setCarrier(request.getCarrier());
        flightRecord.setDepartureTime(request.getDeparture());
        flightRecord.setArrivalTime(request.getArrival());

        flightRecord = flightRecordRepository.save(flightRecord);
        return toFlight.apply(flightRecord);
    }

    private AirportRecord createOrGetAirport(Airport airport) {
        return airportRecordRepository.findById(airport.getAirport())
                .orElseGet(() -> {
                    AirportRecord created = new AirportRecord(
                            airport.getAirport(),
                            airport.getCity(), airport.getCountry());
                    return airportRecordRepository.save(created);
                });
    }

    @Override
    public List<Flight> findFlights(FindFlightRequest request) {
        return flightRecordRepository.findFlightsRequest(
                request.getFrom().getAirport(),
                request.getTo().getAirport(),
                request.getDeparture().atStartOfDay(),
                request.getDeparture().atStartOfDay().plusDays(1),
                request.getArrival().atStartOfDay(),
                request.getArrival().atStartOfDay().plusDays(1))
                .stream()
                .map(toFlight)
                .collect(Collectors.toList());
    }

    @Override
    public Flight findById(Long id) {
        return flightRecordRepository.findById(id)
                .map(toFlight)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Flight> search(String from, String to) {
        if (to == null || to.isEmpty()) {
            return flightRecordRepository.searchFlightsFrom(from)
                    .stream()
                    .map(toFlight)
                    .collect(Collectors.toList());
        }
        if (from == null || from.isEmpty()) {
            return flightRecordRepository.searchFlightsTo(to)
                    .stream()
                    .map(toFlight)
                    .collect(Collectors.toList());
        }
        return flightRecordRepository.searchFlights(from, to)
                .stream()
                .map(toFlight)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        flightRecordRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        flightRecordRepository.deleteAll();
    }

    @Override
    public List<Flight> findAll() {
        return flightRecordRepository.findAll()
                .stream()
                .map(toFlight)
                .collect(Collectors.toList());
    }
}
