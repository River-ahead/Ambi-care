package com.mindcare.service;

import com.mindcare.dto.ChatRequest;
import com.mindcare.dto.ChatResponse;
import com.mindcare.dto.EmotionResult;
import com.mindcare.dto.IntentDecision;
import com.mindcare.entity.ChatMessage;
import com.mindcare.entity.User;
import com.mindcare.repository.ChatHistoryRepository;
import com.mindcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatOrchestratorService {

    private final ModelInferenceService modelInferenceService;
    private final EmotionAnalysisService emotionAnalysisService;
    private final AgenticRAGService agenticRAGService;
    private final PromptTemplateService promptTemplateService;
    private final MCPExternalService mcpExternalService;
    private final MultiModalFusionEngine multiModalFusionEngine;
    private final UserRepository userRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatResponse handleChat(ChatRequest request) {
        Long userId = ensureUser(request.getUserId());
        IntentDecision intentDecision = modelInferenceService.classifyIntent(request.getContent());
        EmotionResult textEmotion = emotionAnalysisService.analyzeAndSave(userId, request.getContent());
        EmotionResult fusedEmotion = multiModalFusionEngine.fuse(textEmotion);

        String answer;
        switch (intentDecision.getIntent()) {
            case "CHAT" -> answer = handleChatIntent(request.getContent());
            case "RISK" -> answer = handleRiskIntent(userId, request.getContent(), fusedEmotion);
            case "CONSULT" -> answer = handleConsultIntent(request.getContent());
            default -> answer = handleConsultIntent(request.getContent());
        }

        ChatMessage msg = new ChatMessage();
        msg.setUserId(userId);
        msg.setUserContent(request.getContent());
        msg.setBotReply(answer);
        msg.setIntent(intentDecision.getIntent());
        msg.setEmotionLabel(fusedEmotion.getLabel());
        msg.setEmotionScore(fusedEmotion.getScore());
        msg.setCreatedAt(LocalDateTime.now());
        chatHistoryRepository.save(msg);

        ChatResponse response = new ChatResponse();
        response.setAnswer(answer);
        response.setIntent(intentDecision.getIntent());
        response.setEmotionLabel(fusedEmotion.getLabel());
        response.setEmotionScore(fusedEmotion.getScore());
        return response;
    }

    public Flux<String> streamAnswer(String fullAnswer) {
        String[] parts = fullAnswer.split("(?<=[。！？!?.])\\s*");
        return Flux.fromArray(parts);
    }

    private Long ensureUser(Long userId) {
        if (userId != null) {
            return userId;
        }
        User user = new User();
        user.setUsername("anonymous-" + System.currentTimeMillis());
        user.setDisplayName("匿名用户");
        return userRepository.save(user).getId();
    }

    private String handleChatIntent(String content) {
        String template = promptTemplateService.safeChatPrompt();
        String prompt = template.replace("{{input}}", content);
        return modelInferenceService.generateAnswer(prompt);
    }

    private String handleConsultIntent(String content) {
        return agenticRAGService.answerWithRag(content);
    }

    private String handleRiskIntent(Long userId, String content, EmotionResult emotionResult) {
        Optional<User> userOpt = userRepository.findById(userId);
        String username = userOpt.map(User::getDisplayName).orElse("未知用户");
        mcpExternalService.sendEmailAlert(
                "counselor@example.com",
                username,
                emotionResult.getScore(),
                emotionResult.getReason()
        );
        return """
                我注意到你刚才的表达中包含较多绝望或自我伤害的内容，这让我非常担心你现在的状态。
                在任何情况下，你的生命和安全都远比一切重要。如果你有立即的危险，请第一时间联系身边可信赖的老师、家人，或拨打当地紧急求助电话。
                同时，强烈建议你尽快与学校心理咨询中心或专业心理医生取得联系，他们能够提供更及时和深入的帮助。
                在你等待专业帮助的过程中，也可以尝试把自己的感受写下来或与信任的人聊一聊，不要一个人独自承受。
                """;
    }
}

