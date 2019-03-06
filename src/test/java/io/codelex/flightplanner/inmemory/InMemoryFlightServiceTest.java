package io.codelex.flightplanner.inmemory;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

import static org.junit.Assert.*;

public class InMemoryFlightServiceTest {
    InMemoryFlightService service = new InMemoryFlightService();

    @BeforeEach
    void setup() {
        service.clearAll();
    }

    @Test
    void should_be_able_add_flight() {
        //given
        AddFlightRequest request = createRequest();
        //when
        Flight flight = service.addFlight(request);
        //then
        assertFlightCreatedProperly(request, flight);
    }

    @Test
    void should_increment_id_when_adding_new_flight() {
        //given
        AddFlightRequest request = createRequest();
        //when
        Flight firstFlight = service.addFlight(request);
        //then
        Assertions.assertEquals(2, firstFlight.getId() + 1);
    }

    @Test
    void should_be_able_to_get_added_flight_by_id() {
        //given
        AddFlightRequest request = createRequest();
        //when
        Flight flight = service.addFlight(request);
        Flight result = service.findById(flight.getId());
        //then
        assertNotNull(result);
        //and
        assertFlightCreatedProperly(request, result);
    }

    private void assertFlightCreatedProperly(AddFlightRequest request, Flight result) {
        Assertions.assertEquals(result.getFrom(), request.getFrom());
        Assertions.assertEquals(result.getTo(), request.getTo());
        Assertions.assertEquals(result.getCarrier(), request.getCarrier());
        Assertions.assertEquals(result.getDepartureTime(), request.getDeparture());
        Assertions.assertEquals(result.getArrivalTime(), request.getArrival());
    }


    @Test
    void should_not_be_able_to_add_duplicated_flight() {
        //given
        AddFlightRequest request = createRequest();
        //when
        service.addFlight(request);
        //then
        Assertions.assertThrows(IllegalStateException.class, () -> service.addFlight(request));
    }

    @Test
    void should_be_able_to_delete_flight_by_id() {
        //given
        AddFlightRequest request = createRequest();
        //when
        Flight flight = service.addFlight(request);
        service.deleteById(flight.getId());
        //then
        assertEquals(service.findAll().size(), 0);
    }

    @Test
    void should_be_able_to_delete_all_flights() {
        //given
        AddFlightRequest request = createRequest();
        //when
        service.addFlight(request);
        service.clearAll();
        //then
        assertTrue(service.isAnyFlightPresent());
    }

    @Test
    void should_be_able_to_search_when_no_values_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("", "");
        //then
        Assertions.assertTrue(flights.isEmpty());
    }

    @Test
    void should_not_find_flights_when_nulls_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search(null, null);
        //then
        assertTrue(flights.isEmpty());
    }

    @Test
    void should_find_flight_when_values_passed() {
        //given
        AddFlightRequest request = createRequest();

        FindFlightRequest findFlightRequest = new FindFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                LocalDate.now(),
                LocalDate.now());

        //when
        Flight flight = service.addFlight(request);
        List<Flight> result = service.findFlights(findFlightRequest);
        //then
        assertEquals(1, result.size());
        assertTrue(result.contains(flight));
    }

    @Test
    void should_find_flight_where_full_airport_name_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search(request.getFrom().getAirport(), "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_full_country_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search(request.getFrom().getCountry(), "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_full_city_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("Riga", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_airport_name_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("RI", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_country_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("Lat", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_city_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("Rig", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_lowercase_airport_name_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("ri", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_uppercase_country_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("LATVI", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_uppercase_city_from_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("RIG", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_airport_name_from_with_space_at_the_end_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("RIX ", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_country_from_with_space_at_the_end_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("Latvi ", "");
        //then
        assertEquals(1, flights.size());
    }

    @Test
    void should_find_flight_where_partial_city_from_with_space_at_the_end_passed() {
        //given
        AddFlightRequest request = createRequest();
        service.addFlight(request);
        //when
        List<Flight> flights = service.search("Rig ", "");
        //then
        assertEquals(1, flights.size());
    }

    private AddFlightRequest createRequest() {
        return new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Ryanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
    }
}

