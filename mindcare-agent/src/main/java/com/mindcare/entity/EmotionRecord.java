package com.mindcare.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mc_emotion_record")
@Getter
@Setter
public class EmotionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String label;

    private Double score;

    private boolean highRisk;

    @Lob
    private String reason;

    private LocalDateTime createdAt;
}

