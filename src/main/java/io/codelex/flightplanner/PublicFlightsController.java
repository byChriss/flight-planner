package io.codelex.flightplanner;

import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
class PublicFlightsController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> search(String from, String to) {
        if (from == null && to == null) {
            return new ResponseEntity<>(flightService.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(flightService.search(from, to), HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<List<Flight>> findFlight(@RequestBody FindFlightRequest request) {
        try {
            return new ResponseEntity<>(flightService.findFlights(request), HttpStatus.OK);
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
}
