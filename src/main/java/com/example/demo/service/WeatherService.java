package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {

    private static final String OPENWEATHERMAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Value("${openweathermap.api_key}") private String openWeatherMapApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<String> getWeather(String city) {
        String url = OPENWEATHERMAP_API_URL + "?q=" + city + "&appid=" + openWeatherMapApiKey;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                // Extract the current weather information
                String weatherDescription = rootNode.path("weather").get(0).path("description").asText();
                double temperature = rootNode.path("main").path("temp").asDouble();
                // Convert temperature from Kelvin to Celsius
                temperature = temperature - 273.15;
                return Optional.of(String.format("Current weather: %s, Temperature: %.2f°C", weatherDescription, temperature));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}



