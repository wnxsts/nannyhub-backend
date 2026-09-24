package com.nannyhub.backend.service.chat;

import com.nannyhub.backend.dto.chat.ChatMessageResponse;
import com.nannyhub.backend.entity.Message;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.ChatRepository;
import com.nannyhub.backend.repository.MessageRepository;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public ChatMessageResponse handleWsMessage(
            Long chatId,
            String email,
            String content
    ) {
        User sender = userRepository.findByEmail(email).orElseThrow();



        Message message = Message.builder()
                .chatId(chatId)
                .senderId(sender.getId())
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        return new ChatMessageResponse(
                chatId,
                sender.getId(),
                content,
                message.getCreatedAt()
        );
    }
}
