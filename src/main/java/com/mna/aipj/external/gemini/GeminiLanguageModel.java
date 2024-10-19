package com.mna.aipj.external.gemini;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiLanguageModel {

    @Value("${googlecloud.project}")
    private String projectID;

    @Value("${googlecloud.location}")
    private String location;

    @Value("${googlecloud.model}")
    private String model;

    public ChatLanguageModel geminiChatModel;

    public ChatLanguageModel getVertexLanguageModel() {
        if (geminiChatModel == null) {
            geminiChatModel = VertexAiGeminiChatModel.builder()
                    .project(this.projectID)
                    .location(this.location)
                    .modelName(this.model)
                    .responseMimeType("application/json")
                    .build();
        }
        return geminiChatModel;
    }
}
