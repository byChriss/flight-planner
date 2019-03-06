package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlightRecordRepositoryTest {

    @Autowired
    FlightRecordRepository repository;

    @Autowired
    AirportRecordRepository airportRecordRepository;
    
    @Test
    public void search_should_not_return_any_results_when_nothing_match() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));

        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);
        //when
        List<FlightRecord> flights = repository.searchFlights("Moscow", "Stockholm");
        //then
        Assertions.assertTrue(flights.isEmpty());
    }

    @Test
    public void search_should_find_when_matched() {
        //given 
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));

        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlights("Riga", "Dubai");

        //then
        Assertions.assertEquals(1, flights.size());
    }

    @Test
    public void search_should_find_when_partial_from_to_passed() {
        //given 
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));

        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlights("Ri", "Dub");

        //then
        Assertions.assertEquals(1, flights.size());
    }

    @Test
    public void search_should_find_when__from_to_passed_with_whitespace() {
        //given 
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));

        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlights("Riga ", "Dubai ");

        //then
        Assertions.assertEquals(1, flights.size());
    }
}
