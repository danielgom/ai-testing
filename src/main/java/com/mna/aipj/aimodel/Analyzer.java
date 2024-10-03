package com.mna.aipj.aimodel;

import com.mna.aipj.aidto.AnalyzerResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Analyzer {

    @SystemMessage(fromResource = "AnalyzerSystemPrompt.txt")
    AnalyzerResponse analyzeMentions(@UserMessage String userMessage);
}
