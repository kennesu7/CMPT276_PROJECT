package com.example.demo;


import java.util.List;
import java.util.Optional;

public record CompletionResponse(Usage usage, List<Choice> choices) {

    public Optional<String> firstAnswer() {
        if (choices == null || choices.isEmpty())
            return Optional.empty();
        return Optional.of(choices.get(0).message().content());
    }

    record Usage(int total_tokens, int prompt_tokens, int completion_tokens) {}
    record Choice(Message message) {}
    record Message(String role, String content) {}
}