package com.mna.aipj.controller;

import com.mna.aipj.dto.TriggerUpdateResponse;
import com.mna.aipj.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping(value = "query:update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TriggerUpdateResponse> triggerExp(@RequestParam("query_id") String queryID) {
        return ResponseEntity.ok(aiService.triggerUpdate(queryID));
    }
}
