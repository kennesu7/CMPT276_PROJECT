package com.example.demo;

import java.util.List;

public record CompletionRequest(String model, List<Message> messages, int max_tokens, Double temperature) {

    public static CompletionRequest defaultWith(String userInput, int max_tokens, Double temperature) {
        Message systemMessage = new Message("system", "Generate a one day itinerary for my visit to the specified city. Provide an activity for the morning, afternoon and night.Additionally when suggesting a place to visit put quotation marks around the name of that place  ");
        Message userMessage = new Message("user", userInput);
        return new CompletionRequest("gpt-4", List.of(systemMessage, userMessage), max_tokens, temperature);
    }

    record Message(String role, String content) {}
}
