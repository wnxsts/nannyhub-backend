package com.nannyhub.backend.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {
    private Long chatId;
    private Long senderId;
    private String content;
}