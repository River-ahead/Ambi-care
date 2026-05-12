package com.mindcare.service;

import com.mindcare.config.InMemoryVectorStoreStub;
import com.mindcare.dto.RagDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Agentic RAG 服务：
 * 1. 调用模型规划是否需要检索；
 * 2. 需要时使用内存向量库检索；
 * 3. 将检索结果拼入 Prompt 生成最终回答。
 */
@Service
@RequiredArgsConstructor
public class AgenticRAGService {

    private final ModelInferenceService modelInferenceService;
    private final PromptTemplateService promptTemplateService;
    private final InMemoryVectorStoreStub vectorStoreStub;

    public String answerWithRag(String userContent) {
        RagDecision decision = modelInferenceService.planRag(userContent);
        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("用户原始问题：").append(userContent).append("\n\n");
        if ("RETRIEVE".equalsIgnoreCase(decision.getAction())) {
            var docs = vectorStoreStub.search(decision.getQuery(), 3);
            String merged = docs.stream()
                    .map(d -> "- " + d.getContent())
                    .collect(Collectors.joining("\n"));
            contextBuilder.append("下列内容是与心理健康相关的参考知识，请在回答时酌情使用：\n")
                    .append(merged).append("\n\n");
        } else {
            contextBuilder.append("模型认为当前问题无需检索知识库，可直接基于对话回复。\n\n");
        }
        String template = promptTemplateService.ragAnswerPrompt();
        String finalPrompt = template
                .replace("{{input}}", userContent)
                .replace("{{context}}", contextBuilder.toString());
        return modelInferenceService.generateAnswer(finalPrompt);
    }
}

