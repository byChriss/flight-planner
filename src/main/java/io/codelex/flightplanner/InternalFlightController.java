package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;

import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/internal-api")
public class InternalFlightController {

    @Autowired
    private FlightService flightService;

    @PutMapping("/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody AddFlightRequest request) {
        try {
            return new ResponseEntity<>(flightService.addFlight(request), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(flightService.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlightById(@PathVariable Long id) {
        flightService.deleteById(id);
       
    }

}
