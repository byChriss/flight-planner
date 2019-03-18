package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightsWithWeather {
    private Flight flight;
    private Weather weather;
    
    @JsonCreator
    public FlightsWithWeather(@JsonProperty("flight") Flight flight,
                              @JsonProperty("weather") Weather weather) {
       this.flight = flight;
       this.weather = weather;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}

