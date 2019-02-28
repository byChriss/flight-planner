package io.codelex.flightplanner;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/testing-api/clear")
    public void clearAll() {
        flightService.clearAll();
    }
}
