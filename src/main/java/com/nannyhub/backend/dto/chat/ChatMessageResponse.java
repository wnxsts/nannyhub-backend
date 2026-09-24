package com.nannyhub.backend.dto.chat;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long chatId,
        Long senderId,
        String content,
        LocalDateTime createdAt
) {}