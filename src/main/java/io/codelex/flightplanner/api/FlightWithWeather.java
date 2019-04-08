package io.codelex.flightplanner.api;

import java.time.LocalDateTime;

public class FlightWithWeather extends Flight {
    private final Weather weather;

    public FlightWithWeather(Long id,
                             Airport from,
                             Airport to,
                             String carrier,
                             LocalDateTime departure,
                             LocalDateTime arrival,
                             Weather weather) {
        super(id, from, to, carrier, departure, arrival);
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
