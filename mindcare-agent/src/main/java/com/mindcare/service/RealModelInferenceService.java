package com.mindcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindcare.dto.IntentDecision;
import com.mindcare.dto.RagDecision;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 使用 Spring AI ChatClient 的真实模型推理服务。
 * 在 mindcare.mock-model=false 时启用。
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "mindcare", name = "mock-model", havingValue = "false")
public class RealModelInferenceService implements ModelInferenceService {

    private final ChatClient ollamaChatClient;
    private final PromptTemplateService promptTemplateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public IntentDecision classifyIntent(String userContent) {
        String prompt = promptTemplateService.intentPrompt().replace("{{input}}", userContent);
        String raw = ollamaChatClient.prompt().user(prompt).call().content();
        try {
            return objectMapper.readValue(raw, IntentDecision.class);
        } catch (Exception e) {
            log.error("解析意图识别 JSON 失败，raw={}", raw, e);
            IntentDecision fallback = new IntentDecision();
            fallback.setIntent("CONSULT");
            fallback.setReason("解析失败，使用默认 CONSULT");
            return fallback;
        }
    }

    @Override
    public RagDecision planRag(String userContent) {
        String prompt = promptTemplateService.ragPlanPrompt().replace("{{input}}", userContent);
        String raw = ollamaChatClient.prompt().user(prompt).call().content();
        try {
            return objectMapper.readValue(raw, RagDecision.class);
        } catch (Exception e) {
            log.error("解析 RAG 规划 JSON 失败，raw={}", raw, e);
            RagDecision fallback = new RagDecision();
            fallback.setAction("RETRIEVE");
            fallback.setQuery(userContent);
            fallback.setNeedMultiStep(false);
            return fallback;
        }
    }

    @Override
    public String generateAnswer(String promptWithContext) {
        return ollamaChatClient.prompt().user(promptWithContext).call().content();
    }
}

