package com.mindcare.controller;

import com.mindcare.dto.ChatRequest;
import com.mindcare.dto.ChatResponse;
import com.mindcare.service.ChatOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatOrchestratorService chatOrchestratorService;

    @PostMapping("/once")
    public ChatResponse chatOnce(@RequestBody ChatRequest request) {
        return chatOrchestratorService.handleChat(request);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam("userId") Long userId,
                                   @RequestParam("content") String content) {
        ChatRequest req = new ChatRequest();
        req.setUserId(userId);
        req.setContent(content);
        ChatResponse full = chatOrchestratorService.handleChat(req);
        return chatOrchestratorService.streamAnswer(full.getAnswer());
    }
}

