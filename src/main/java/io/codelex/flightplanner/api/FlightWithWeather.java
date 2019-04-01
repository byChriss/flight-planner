package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FlightWithWeather extends Flight {
    private final Weather weather;

    public FlightWithWeather(@JsonProperty("id") Long id,
                             @JsonProperty("from") Airport from,
                             @JsonProperty("to") Airport to,
                             @JsonProperty("carrier") String carrier,
                             @JsonProperty("departure") LocalDateTime departure,
                             @JsonProperty("arrival") LocalDateTime arrival,
                             @JsonProperty("weather") Weather weather) {
        super(id, from, to, carrier, departure, arrival);
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
