package com.mindcare.service;

import com.mindcare.entity.ChatMessage;
import com.mindcare.entity.EmotionRecord;
import com.mindcare.repository.ChatHistoryRepository;
import com.mindcare.repository.EmotionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final EmotionRecordRepository emotionRecordRepository;

    public List<ChatMessage> findAllMessages() {
        return chatHistoryRepository.findAll();
    }

    public List<ChatMessage> findMessagesByUser(Long userId) {
        return chatHistoryRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    public List<EmotionRecord> findEmotionsByUser(Long userId) {
        return emotionRecordRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }
}

