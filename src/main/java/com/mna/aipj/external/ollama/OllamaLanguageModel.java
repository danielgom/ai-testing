package com.mna.aipj.external.ollama;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OllamaLanguageModel {

    @Value("${ollama.baseurl}")
    private String url;

    @Value("${ollama.model}")
    private String model;

    public ChatLanguageModel chatLanguageModel;

    public ChatLanguageModel getOllamaLanguageModel() {
        if (chatLanguageModel == null) {
            chatLanguageModel = OllamaChatModel.builder()
                    .baseUrl(this.url)
                    .modelName(this.model)
                    .temperature(0.5)
                    .timeout(Duration.ofSeconds(360))
                    .format("json")
                    .build();
        }
        return chatLanguageModel;
    }
}
