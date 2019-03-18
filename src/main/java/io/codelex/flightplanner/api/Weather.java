package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {

    private Double temperature;
    private Double precipitation;
    private Double windSpeed;
    private String weatherCondition;

    public Weather(@JsonProperty("avgtemp_c") Double temperature,
                   @JsonProperty("precip_mm") Double precipitation,
                   @JsonProperty("wind_kph") Double windSpeed,
                   @JsonProperty("condition") String weatherCondition) {

        this.temperature = temperature;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition;
    }

    public Weather() {
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
}
