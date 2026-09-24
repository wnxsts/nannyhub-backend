package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.chat.ChatMessageRequest;
import com.nannyhub.backend.dto.chat.ChatMessageResponse;
import com.nannyhub.backend.entity.Chat;
import com.nannyhub.backend.entity.Message;
import com.nannyhub.backend.repository.ChatRepository;
import com.nannyhub.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request) {
        if (request.getChatId() == null || request.getSenderId() == null || request.getContent() == null) {
            return;
        }

        Long dialogId = request.getChatId();

        Chat chat = chatRepository.findByMatchId(dialogId)
                .orElseGet(() -> chatRepository.save(
                        Chat.builder()
                                .matchId(dialogId)
                                .build()
                ));

        Message saved = messageRepository.save(
                Message.builder()
                        .chatId(chat.getId())
                        .senderId(request.getSenderId())
                        .content(request.getContent())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        ChatMessageResponse response = new ChatMessageResponse(
                dialogId,
                saved.getSenderId(),
                saved.getContent(),
                saved.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + dialogId, response);
    }
}
