package com.mna.aipj.aimodel;

import com.mna.aipj.aidto.ClassifierResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Classifier {

    @SystemMessage(fromResource = "ClassifierSystemPrompt.txt")
    ClassifierResponse classifyMentions(@UserMessage String userMessage);
}
