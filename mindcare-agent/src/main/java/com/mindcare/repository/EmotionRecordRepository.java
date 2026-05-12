package com.mindcare.repository;

import com.mindcare.entity.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {

    List<EmotionRecord> findByUserIdOrderByCreatedAtAsc(Long userId);
}

