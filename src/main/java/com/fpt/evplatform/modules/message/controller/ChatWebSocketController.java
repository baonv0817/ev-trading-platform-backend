package com.fpt.evplatform.modules.message.controller;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.message.dto.ChatMessage;
import com.fpt.evplatform.modules.message.dto.request.MessageRequest;
import com.fpt.evplatform.modules.message.entity.Message;
import com.fpt.evplatform.modules.message.mapper.MessageMapper;
import com.fpt.evplatform.modules.message.repository.MessageRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatWebSocketController {

    MessageRepository messageRepository;
    MessageMapper messageMapper;
    UserRepository userRepository;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage, Principal principal) {
        if (principal == null) {
            log.error("❌ Principal is null — WebSocket authentication missing!");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String senderUsername = principal.getName(); // from JWT
        log.info("Authenticated sender: {}", senderUsername);

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new AppException(ErrorCode.SENDER_NOT_FOUND));

        User receiver = userRepository.findById(chatMessage.getReceiverId())
                .orElseThrow(() -> new AppException(ErrorCode.RECEIVER_NOT_FOUND));

        Message entity = messageMapper.toMessage(
                MessageRequest.builder()
                        .senderId(sender.getUserId())
                        .receiverId(receiver.getUserId())
                        .body(chatMessage.getContent())
                        .build(),
                sender,
                receiver
        );

        entity.setConversationKey(chatMessage.getConversationKey());
        entity.setSentAt(LocalDateTime.now());
        messageRepository.save(entity);

        chatMessage.setSenderId(sender.getUserId());
        chatMessage.setSentAt(entity.getSentAt().toString());

        // Send to both sender and receiver
        messagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/messages", chatMessage);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages", chatMessage);
    }
}
