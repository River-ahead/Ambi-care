package com.mindcare.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Prompt 模板统一加载服务
 * 所有提示词统一放在 resources/prompts 下，便于维护和面试讲解。
 */
@Service
@Slf4j
public class PromptTemplateService {

    public String loadPrompt(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/" + filename);
            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("加载 Prompt 模板失败: {}", filename, e);
            return "";
        }
    }

    public String intentPrompt() {
        return loadPrompt("intent_classify.txt");
    }

    public String emotionPrompt() {
        return loadPrompt("emotion_classify.txt");
    }

    public String ragPlanPrompt() {
        return loadPrompt("rag_plan.txt");
    }

    public String ragAnswerPrompt() {
        return loadPrompt("rag_answer.txt");
    }

    public String safeChatPrompt() {
        return loadPrompt("safe_chat.txt");
    }
}

