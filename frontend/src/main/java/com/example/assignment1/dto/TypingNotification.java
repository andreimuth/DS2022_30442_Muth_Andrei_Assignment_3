package com.example.assignment1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypingNotification {
    private String fromUsername;
    private String toUsername;
    private Boolean isTyping;
}
