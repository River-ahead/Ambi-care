package com.mindcare.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mc_chat_message")
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Lob
    private String userContent;

    @Lob
    private String botReply;

    private String intent;

    private String emotionLabel;

    private Double emotionScore;

    private LocalDateTime createdAt;
}

