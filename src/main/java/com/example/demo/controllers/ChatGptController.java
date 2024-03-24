package com.example.demo.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.CompletionResponse;
import com.example.demo.CompletionRequest;
import com.example.demo.dto.ChatMessageDTO;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ChatGptController {
	
	private static final String MAIN_PAGE = "chat";
	
	@GetMapping(path = "/chat")
	public String index() {
		return MAIN_PAGE;
	}
	
	@PostMapping(path = "/chat")
public String chat(Model model, @ModelAttribute ChatMessageDTO dto) {
    try {
        String city = dto.city().trim().replaceAll(",+$", ""); // Remove any trailing commas
        List<String> genres = dto.genre(); // Get the genres list
        String message = buildMessage(city, genres);
        model.addAttribute("request", city); // Set the cleaned city name
        model.addAttribute("response", chatWithGpt3(message));
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("response", "Error in communication with OpenAI ChatGPT API: " + e.getMessage());
    }
    return MAIN_PAGE;
}





	private String buildMessage(String city, List<String> genres) {
		String interest = (genres != null && !genres.isEmpty()) ? "I am interested in " + String.join(", ", genres) : "";
		city = city != null ? city.trim().replaceAll(",$", "") : "";
		return String.format("%s, %s", city, interest).trim();
	}

	
	
	@Autowired private ObjectMapper jsonMapper;
	 private String openaiApiKey ="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";
	private HttpClient client = HttpClient.newHttpClient();
	private static final URI CHATGPT_URI = URI.create("https://api.openai.com/v1/chat/completions");
	
	private String chatWithGpt3(String message) throws Exception {
		// Hardcoded max tokens and temperature values.
		int maxTokens = 300;
		double temperature = 0.7; // Set your desired temperature value here
	
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
	
}
