package com.mindcare.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 简单的内存向量库 Stub，用于模拟心理知识检索。
 */
public class InMemoryVectorStoreStub {

    private final List<VectorDoc> docs = new ArrayList<>();

    public InMemoryVectorStoreStub() {
        docs.add(new VectorDoc(
                "doc1",
                "当你感到焦虑时，尝试做几次深呼吸，慢慢吸气、停留三秒、再缓慢呼出，可以帮助身体放松。",
                List.of("焦虑", "呼吸放松", "情绪调节")
        ));
        docs.add(new VectorDoc(
                "doc2",
                "如果长期处于失眠状态，建议规律作息，睡前避免使用电子产品，如有需要可以寻求专业心理咨询或医生帮助。",
                List.of("失眠", "作息", "睡眠卫生")
        ));
        docs.add(new VectorDoc(
                "doc3",
                "当你感到情绪低落时，可以尝试和信任的人聊聊天，适度表达自己的感受会有助于情绪疏解。",
                List.of("抑郁", "低落", "倾诉")
        ));
    }

    public List<VectorDoc> search(String query, int topK) {
        return docs.stream()
                .sorted(Comparator.comparingInt((VectorDoc d) -> matchScore(query, d)).reversed())
                .limit(topK)
                .toList();
    }

    private int matchScore(String query, VectorDoc doc) {
        int score = 0;
        String lowerQuery = query.toLowerCase();
        if (doc.getContent().toLowerCase().contains(lowerQuery)) {
            score += 5;
        }
        for (String tag : doc.getTags()) {
            if (lowerQuery.contains(tag.toLowerCase())) {
                score += 2;
            }
        }
        return score;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VectorDoc {
        private String id;
        private String content;
        private List<String> tags;
    }
}

