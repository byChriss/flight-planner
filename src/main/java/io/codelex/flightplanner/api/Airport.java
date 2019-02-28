package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Airport {
    private String country;
    private String city;
    private String airport;

    public Airport(@JsonProperty("country") String country,
                   @JsonProperty("city") String city,
                   @JsonProperty("airport") String airport) {
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
        return Objects.equals(getCountry(), airport1.getCountry())
                && Objects.equals(getCity(), airport1.getCity())
                && Objects.equals(getAirport(), airport1.getAirport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCity(), getAirport());
    }
}
