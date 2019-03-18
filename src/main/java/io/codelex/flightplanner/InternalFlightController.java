package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;

import io.codelex.flightplanner.api.Flight;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/internal-api")
public class InternalFlightController {

    private final FlightService service;

    InternalFlightController(FlightService service) {
        this.service = service;
    }

    @PutMapping("/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody @Valid AddFlightRequest request) {
        if (request.getArrival().isBefore(request.getDeparture())
                || request.getDeparture().isEqual(request.getArrival())
                || request.getFrom().getAirport().toLowerCase().trim().equals(request.getTo().getAirport().toLowerCase().trim())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else
            try {
                return new ResponseEntity<>(service.addFlight(request), CREATED);
            } catch (IllegalStateException e) {
                return new ResponseEntity<>(CONFLICT);
            }
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlightById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
