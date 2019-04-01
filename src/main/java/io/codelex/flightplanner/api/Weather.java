package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Weather {
    private Double averageTemperature;
    private Double maxWind;
    private Double precipitation;
    private String condition;


    public Weather(@JsonProperty("avgtemp_c") Double averageTemperature,
                   @JsonProperty("maxwind_kph") Double maxWind,
                   @JsonProperty("precip_mm") Double precipitation,
                   @JsonProperty("condition") String condition) {

        this.averageTemperature = averageTemperature;
        this.maxWind = maxWind;
        this.precipitation = precipitation;
        this.condition = condition;

    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public Double getMaxWind() {
        return maxWind;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public String getCondition() {
        return condition;
    }
}
