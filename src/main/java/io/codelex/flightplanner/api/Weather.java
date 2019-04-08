package io.codelex.flightplanner.api;


public class Weather {
    private Double averageTemperature;
    private Double maxWind;
    private Double precipitation;
    private String condition;

    public Weather(Double averageTemperature,
                   Double maxWind,
                   Double precipitation,
                   String condition) {
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
