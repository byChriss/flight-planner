package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class Airport {
    @NotEmpty
    private String country;
    @NotEmpty
    private String city;
    @NotEmpty
    private String airport;

    public Airport(@JsonProperty("airport") String airport,
                   @JsonProperty("city") String city,
                   @JsonProperty("country") String country) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAirport() {
        return airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return getAirport().equals(airport1.getAirport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAirport());
    }
}
