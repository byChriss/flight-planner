package io.codelex.flightplanner.repository;


import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;

import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RepositoryFlightServiceTest {
    FlightRecordRepository flightRecordRepository = Mockito.mock(FlightRecordRepository.class);
    AirportRecordRepository airportRecordRepository = Mockito.mock(AirportRecordRepository.class);

    RepositoryFlightService service = new RepositoryFlightService(flightRecordRepository, airportRecordRepository);

    @Test
    void should_throw_exception_when_flight_present() {
        //given
        AddFlightRequest req = new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        Mockito.when(flightRecordRepository.isFlightPresent(req.getFrom().getAirport(),
                req.getTo().getAirport(),
                req.getDeparture(),
                req.getArrival(),
                req.getCarrier())).thenReturn(true);


        //expect
        Assertions.assertThrows(IllegalStateException.class, () -> service.addFlight(req));
    }

    @Test
    void should_be_able_to_add_flight() {
        //given
        AddFlightRequest req = new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        Mockito.when(airportRecordRepository.save(Mockito.any()))
                .thenAnswer((Answer) invocation -> invocation.getArguments()[0]);
        Mockito.when(airportRecordRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(flightRecordRepository.save(Mockito.any()))
                .thenAnswer((Answer) invocation -> invocation.getArguments()[0]);

        Flight flight = service.addFlight(req);

        Assertions.assertEquals(req.getFrom(), flight.getFrom());
        Assertions.assertEquals(req.getTo(), flight.getTo());
        Assertions.assertEquals(req.getCarrier(), flight.getCarrier());
        Assertions.assertEquals(req.getDeparture(), flight.getDepartureTime());
        Assertions.assertEquals(req.getArrival(), flight.getArrivalTime());
    }

    @Test
    void should_find_flight_when_from_and_to_passed() {
        //given
        List<FlightRecord> someFlight = new ArrayList<>();
        someFlight.add(createFlightRecord());

        Answer<List<FlightRecord>> answer = invocation -> someFlight;
        Flight flight = createFlight();
        Mockito.when(flightRecordRepository.searchFlights(Mockito.any(), Mockito.any())).thenAnswer((answer));

        //when
        List<Flight> returnList = service.search("Riga", "Stockholm");

        //expect
        assertion(flight, returnList);
    }

    @Test
    void should_find_flight_when_from_passed() {
        //given
        List<FlightRecord> someFlight = new ArrayList<>();
        someFlight.add(createFlightRecord());

        Answer<List<FlightRecord>> answer = invocation -> someFlight;
        Flight flight = createFlight();
        Mockito.when(flightRecordRepository.searchFlightsFrom(Mockito.any())).thenAnswer((answer));

        //when
        List<Flight> returnList = service.search("Riga", null);

        //expect
        assertion(flight, returnList);
    }

    @Test
    void should_find_flight_when_to_passed() {
        //given
        List<FlightRecord> someFlight = new ArrayList<>();
        someFlight.add(createFlightRecord());

        Answer<List<FlightRecord>> answer = invocation -> someFlight;
        Flight flight = createFlight();
        Mockito.when(flightRecordRepository.searchFlightsTo(Mockito.any())).thenAnswer((answer));

        //when
        List<Flight> returnList = service.search(null, "Stockholm");

        //expect
        assertion(flight, returnList);
    }

    @Test
    void should_find_flight() {
        //given
        FindFlightRequest request = new FindFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Latvia", "Riga", "RIX"),
                LocalDate.of(2019, 1, 2),
                LocalDate.of(2019, 1, 2));

        List<FlightRecord> someFlight = new ArrayList<>();
        someFlight.add(createFlightRecord());

        Answer<List<FlightRecord>> answer = invocation -> someFlight;
        Flight flight = createFlight();

        Mockito.when(flightRecordRepository.findFlightsRequest(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenAnswer((answer));
        //when
        List<Flight> returnList = service.findFlights(request);
        //expect
        assertion(flight, returnList);
    }

    private void assertion(Flight flight, List<Flight> returnList) {
        Assertions.assertEquals(returnList.get(0).getFrom(), flight.getFrom());
        Assertions.assertEquals(returnList.get(0).getTo(), flight.getTo());
        Assertions.assertEquals(returnList.get(0).getCarrier(), flight.getCarrier());
        Assertions.assertEquals(returnList.get(0).getDepartureTime(), flight.getDepartureTime());
        Assertions.assertEquals(returnList.get(0).getArrivalTime(), flight.getArrivalTime());
    }

    private Flight createFlight() {
        return new Flight(1L,
                new Airport("RIX", "Riga", "Latvia"),
                new Airport("ARN", "Stockholm", "Sweden"),
                "Ryanair",
                LocalDateTime.of(2019, 1, 2, 14, 20),
                LocalDateTime.of(2019, 1, 2, 14, 20).plusHours(1));

    }

    private FlightRecord createFlightRecord() {
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFrom(new AirportRecord("RIX", "Riga", "Latvia"));
        flightRecord.setTo(new AirportRecord("ARN", "Stockholm", "Sweden"));
        flightRecord.setCarrier("Ryanair");
        flightRecord.setDepartureTime(LocalDateTime.of(2019, 1, 2, 14, 20));
        flightRecord.setArrivalTime(LocalDateTime.of(2019, 1, 2, 14, 20).plusHours(1));
        return flightRecord;
    }
}