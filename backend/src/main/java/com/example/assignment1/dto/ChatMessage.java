package com.example.assignment1.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChatMessage {
    private String from;
    private String to;
    private String text;
    private Boolean seen;
}
