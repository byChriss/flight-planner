package io.codelex.flightplanner;

import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.api.FlightsWithWeather;
import io.codelex.flightplanner.api.Weather;
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
    private WeatherService weatherService = new WeatherService();

    PublicFlightsController(FlightService service) {
        this.service = service;
    }


    @GetMapping("/flights/search")
    public ResponseEntity <List<Flight>> searchTrip(@RequestParam(value = "from", defaultValue = "") String from, @RequestParam(value = "to", defaultValue = "") String to) {
        if (from == null && to == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(service.search(from, to), HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<List<FlightsWithWeather>> findFlight(@RequestBody @Valid FindFlightRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(createList(service.findFlights(request)), HttpStatus.OK);
        } catch (NullPointerException e) {
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

    private List<FlightsWithWeather> createList(List<Flight> flights) {
        List<FlightsWithWeather> flightWeatherMap = new ArrayList<>();
        for (int i = 0; i < flights.size(); i++) {
            Weather weather = weatherService.getWeather(flights.get(i).getTo().getCity());
            flightWeatherMap.add(new FlightsWithWeather(flights.get(i), weather));
        }
        return flightWeatherMap;
    }

}
