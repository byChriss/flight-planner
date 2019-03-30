package io.codelex.flightplanner.weather;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ForecastResponse {
    private final Forecast forecast;


    @JsonCreator
    public ForecastResponse(@JsonProperty("forecast") Forecast forecast) {
        this.forecast = forecast;
    }

    Forecast getForecast() {
        return forecast;
    }

    public static class Forecast {
        private final List<ForecastDay> forecastDays;

        @JsonCreator
        public Forecast(@JsonProperty("forecastday") List<ForecastDay> forecastDays) {
            this.forecastDays = forecastDays;
        }

        List<ForecastDay> getForecastDays() {
            return forecastDays;
        }

        public static class ForecastDay {
            private final Day day;

            @JsonCreator
            public ForecastDay(@JsonProperty("day") Day day) {
                this.day = day;
            }

            Day getDay() {
                return day;
            }

            public static class Day {
                private final Double averageTemperature;
                private final Double maxWind;
                private final Double precipitation;
                private final Condition condition;

                @JsonCreator
                public Day(@JsonProperty("avgtemp_c") Double averageTemperature,
                           @JsonProperty("maxwind_kph") Double maxWind,
                           @JsonProperty("totalprecip_mm") Double precipitation,
                           @JsonProperty("condition") Condition condition) {
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

                public Condition getCondition() {
                    return condition;
                }

                public static class Condition {
                    private final String text;

                    @JsonCreator
                    public Condition(@JsonProperty("text") String text) {
                        this.text = text;
                    }

                    String getText() {
                        return text;
                    }
                }
            }
        }
    }
}
