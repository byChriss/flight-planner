package io.codelex.flightplanner;

import io.codelex.flightplanner.api.Weather;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WeatherService {
    RestTemplate restTemplate = new RestTemplate();

    public Weather getWeather(String city) {
        String weatherUrl = "https://api.apixu.com/v1/current.json?key=f91fc29208054c76871145942190903&q=" + city;
        ResponseEntity<String> response = restTemplate.getForEntity(weatherUrl, String.class);
        String json = response.getBody();

        try {
            Weather forecast = new Weather();
            JSONObject obj = new JSONObject(json);
            forecast.setWeatherCondition(obj.getJSONObject("current").getJSONObject("condition").getString("text"));
            forecast.setTemperature(obj.getJSONObject("current").getDouble("temp_c"));
            forecast.setWindSpeed(obj.getJSONObject("current").getDouble("wind_kph"));
            forecast.setPrecipitation(obj.getJSONObject("current").getDouble("precip_mm"));
            return forecast;
        } catch (Exception e) {
            return null;
        }

    }
}

