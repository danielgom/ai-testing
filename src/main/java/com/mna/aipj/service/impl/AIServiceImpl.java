package com.mna.aipj.service.impl;

import com.mna.aipj.dto.AuthorMention;
import com.mna.aipj.dto.TriggerUpdateResponse;
import com.mna.aipj.dto.exception.UserException;
import com.mna.aipj.external.brandwatch.BrandWatchMention;
import com.mna.aipj.external.brandwatch.Brandwatch;
import com.mna.aipj.external.brandwatch.BrandwatchMentionResponse;
import com.mna.aipj.service.AIService;
import com.mna.aipj.utils.ExcelReader;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class AIServiceImpl implements AIService {

    private final ResourceLoader resourceLoader;

    private final ExcelReader excelReader;

    private final Brandwatch brandwatch;

    private final ChatLanguageModel chatLanguageModel;

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    @Override
    public void readExcel() {
        List<AuthorMention> authorMentions = getAuthorMentions();

        String entry = IntStream.rangeClosed(1, authorMentions.size())
                .mapToObj(idx -> String.format("%d -- %s --\n", idx, authorMentions.get(idx - 1).getTitle()))
                .collect(Collectors.joining());

        logger.info(entry);

        /*
        Analyzer analyzer = AiServices.create(Analyzer.class, chatLanguageModel);
        System.out.println(analyzer.analyzeMentions(entry));

         */

        /*
        ComposablePrompt composablePrompt = AiServices.create(ComposablePrompt.class, chatLanguageModel);
        String answer = composablePrompt.chat("Hello there, I'd like know who you are and what you do!");

         */

        //logger.info(answer);
    }

    @Override
    public TriggerUpdateResponse triggerUpdate(String queryID) {
        BrandwatchMentionResponse mentions = brandwatch.getQueryMentions(queryID);
        List<BrandWatchMention> filteredMentions = mentions.getResults().stream()
                .filter(mention -> !mention.getContentSourceName().equals("X"))
                .peek(mention -> System.out.println(mention.getSnippet()))
                .toList();

        System.out.println(filteredMentions.size());

        return TriggerUpdateResponse.builder().build();
    }

    public List<AuthorMention> getAuthorMentions() {
        Resource resource = resourceLoader.getResource("classpath:example.xlsx");

        try {
            File file = resource.getFile();
            return excelReader.excelToAuthorMentions(file.getAbsolutePath());
        } catch (IOException e) {
            throw new UserException("Error accessing the Excel file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new UserException("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
