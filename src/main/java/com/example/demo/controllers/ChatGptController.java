package com.example.demo.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.util.Optional;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.CompletionResponse;
import com.example.demo.CompletionRequest;
import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.models.Itinerary;
import com.example.demo.models.ItineraryRepository;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import com.example.demo.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ChatGptController {

	@Autowired // not originally added
	private final ItineraryRepository ItineraryRepository;
	@Autowired // not originally added delete if need
    private final UserRepository UserRepository;

	@Autowired
	private final WeatherService weatherService;


	//@AutoWired was here originally
    public ChatGptController(ItineraryRepository itineraryRepository, UserRepository userRepository, WeatherService weatherService) {
        this.ItineraryRepository = itineraryRepository;
        this.UserRepository = userRepository;
		this.weatherService = weatherService;
    }
	private static final String MAIN_PAGE = "schedule";
	
	@GetMapping(path = "/chat")
	public String index() {
		return MAIN_PAGE;
	}
	
	@GetMapping("/map")
	public String map(Model model){

			return "mapTest";
	}
	
	
	@PostMapping(path = "/chat")
	public String chat(Model model, @ModelAttribute ChatMessageDTO dto) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userEmail = authentication.getName();
			User user = UserRepository.findByEmail(userEmail);
			String email = UserRepository.findByEmail(userEmail).getEmail();
			String city = dto.city().trim().replaceAll(",+$", ""); // Remove any trailing commas
			List<String> genre = dto.genres(); // Get the genres list
			
			Optional<String> weatherDataOptional = weatherService.getWeather(city);
        	String weatherData = "Weather data not available";
        	if (weatherDataOptional.isPresent()) {
            // Extract only the "Current weather" part of the weather information
            weatherData = weatherDataOptional.map(data -> {
                // Assuming the format is "Current weather: ..., Temperature: ...°C"
                	String[] parts = data.split(", ");
                	return parts[0]; // This will be "Current weather: ..."
            	}).orElse("Weather data not available");
        	}

        	// Include the weather data in the model
        	model.addAttribute("weather", weatherData);
			
			if(weatherData != null){
				//include the weather data in the message
				String message = buildMessage(city, genre, weatherData);
				String generatedItinerary = chatWithGpt3(message);
				ArrayList<String> city_list=extractPlaceNames(generatedItinerary);
				

				
				Itinerary itinerary = new Itinerary(user, email, generatedItinerary);
				ItineraryRepository.save(itinerary);
				model.addAttribute("centerCity",city);
				model.addAttribute("cities",city_list);
				model.addAttribute("request", city); // Set the cleaned city name
				model.addAttribute("response", generatedItinerary);
			} else { 
				model.addAttribute("response", "Error getching weather data for " + city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("response", "Error in communication with OpenAI ChatGPT API: " + e.getMessage());
		}
		return MAIN_PAGE; //
	}


	private String buildMessage(String city, List<String> genre, String weatherData) {
		String interest = (genre != null && !genre.isEmpty()) ? "I am interested in " + String.join(", ", genre) : "";
		city = city != null ? city.trim().replaceAll(",$", "") : "";
		return String.format("%s, %s, Weather condition is: %s", city, interest, weatherData).trim();
	}

	
	
	@Autowired private ObjectMapper jsonMapper;
    @Value("${openai.api_key}") private String openaiApiKey;
	private HttpClient client = HttpClient.newHttpClient();
	private static final URI CHATGPT_URI = URI.create("https://api.openai.com/v1/chat/completions");
	
	private String chatWithGpt3(String message) throws Exception {
		// Hardcoded max tokens and temperature values.
		int maxTokens = 300;
		double temperature = 0.2; // Set your desired temperature value here
	
		var request = HttpRequest.newBuilder()
				.uri(CHATGPT_URI)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
				.POST(chatMessageAsPostBody(message, maxTokens, temperature)) 
				.build();
		
		var responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
		return completionResponse.firstAnswer().orElseThrow();
	}
	
	private BodyPublisher chatMessageAsPostBody(String message, int maxTokens, double temperature) throws JsonProcessingException {
		var completion = CompletionRequest.defaultWith(message, maxTokens, temperature); 
		return BodyPublishers.ofString(jsonMapper.writeValueAsString(completion));
	}

	

	public ArrayList<String> extractPlaceNames(String responseText) {
		// Regex pattern to match any text enclosed in quotation marks
		Pattern pattern = Pattern.compile("\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(responseText);
	
		ArrayList<String> placeNames = new ArrayList<>();
	
		while (matcher.find()) {
			// Add the text found within quotation marks to the list
			placeNames.add(matcher.group(1).trim());
		}
	
		return placeNames;
	}
    
	
}
