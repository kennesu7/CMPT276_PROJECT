package com.example.demo;

import java.util.List;

public record CompletionRequest(String model, List<Message> messages, int max_tokens, Double temperature) {

    public static CompletionRequest defaultWith(String userInput, int max_tokens, Double temperature) {
        Message systemMessage = new Message("system", "Generate an itinerary for my visit to the specified city and provide a pricepoint in brackets.");
        Message userMessage = new Message("user", userInput);
        return new CompletionRequest("gpt-4", List.of(systemMessage, userMessage), max_tokens, temperature);
    }

    record Message(String role, String content) {}
}
