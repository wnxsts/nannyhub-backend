package com.nannyhub.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nannyhub.backend.dto.chat.ChatMessageRequest;
import com.nannyhub.backend.entity.Message;
import com.nannyhub.backend.repository.MessageRepository;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.*;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<Long, List<WebSocketSession>> sessions = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageRepository messageRepository;

    public ChatWebSocketHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageRequest req = mapper.readValue(message.getPayload(), ChatMessageRequest.class);

        Message saved = messageRepository.save(
                Message.builder()
                        .chatId(req.getChatId())
                        .senderId(req.getSenderId())
                        .content(req.getContent())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        String response = mapper.writeValueAsString(saved);

        sessions.getOrDefault(req.getChatId(), List.of())
                .forEach(s -> {
                    try {
                        s.sendMessage(new TextMessage(response));
                    } catch (Exception ignored) {}
                });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long chatId = Long.valueOf(
                Objects.requireNonNull(session.getUri()).getQuery().split("=")[1]
        );

        sessions.computeIfAbsent(chatId, k -> new ArrayList<>()).add(session);
    }
}