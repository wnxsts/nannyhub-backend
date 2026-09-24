package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.chat.SendMessageRequest;
import com.nannyhub.backend.entity.Chat;
import com.nannyhub.backend.entity.Message;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.ChatRepository;
import com.nannyhub.backend.repository.MessageRepository;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private Chat resolveByDialogId(Long dialogId) {
        return chatRepository.findByMatchId(dialogId)
                .orElseGet(() -> chatRepository.save(
                        Chat.builder()
                                .matchId(dialogId)
                                .build()
                ));
    }

    @PostMapping("/{chatId}/messages")
    public Message sendMessage(
            @PathVariable Long chatId,
            @RequestBody SendMessageRequest request,
            Authentication auth
    ) {
        User me = userRepository.findByEmail(auth.getName()).orElseThrow();
        Chat chat = resolveByDialogId(chatId);

        return messageRepository.save(
                Message.builder()
                        .chatId(chat.getId())
                        .senderId(me.getId())
                        .content(request.getContent())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{chatId}/messages")
    public List<Message> getMessages(@PathVariable Long chatId) {
        Chat chat = resolveByDialogId(chatId);
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
    }
}
