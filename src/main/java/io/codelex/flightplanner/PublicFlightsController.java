package io.codelex.flightplanner;

import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.api.FlightWithWeather;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api")
public
class PublicFlightsController {

    private final FlightService service;
    private final FlightDecorator flightDecorator;

    public PublicFlightsController(FlightService service, FlightDecorator flightDecorator) {
        this.service = service;
        this.flightDecorator = flightDecorator;
    }

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> searchTrip(@RequestParam(value = "from", defaultValue = "") String from, @RequestParam(value = "to", defaultValue = "") String to) {
        if (from == null && to == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(service.search(from, to), HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<List<FlightWithWeather>> findFlight(@RequestBody @Valid FindFlightRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(flightDecorator.findFlight(request), HttpStatus.OK);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    
}
