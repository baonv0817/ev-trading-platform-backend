package com.fpt.evplatform.modules.message.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.message.dto.request.MessageRequest;
import com.fpt.evplatform.modules.message.dto.response.ConversationResponse;
import com.fpt.evplatform.modules.message.dto.response.MessageResponse;
import com.fpt.evplatform.modules.message.entity.Message;
import com.fpt.evplatform.modules.message.mapper.MessageMapper;
import com.fpt.evplatform.modules.message.repository.MessageRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    MessageRepository messageRepository;
    UserRepository userRepository;
    MessageMapper messageMapper;

    public MessageResponse sendMessage(MessageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Message message = messageMapper.toMessage(request, sender, receiver);
        return messageMapper.toMessageResponse(messageRepository.save(message));
    }

    public List<MessageResponse> getConversation(Integer user1Id, Integer user2Id) {
        String key = messageMapper.generateConversationKey(user1Id, user2Id);
        return messageRepository.findByConversationKeyOrderBySentAtAsc(key)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    public List<MessageResponse> getMessagesOfUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return messageRepository.findBySenderOrReceiverOrderBySentAtDesc(user, user)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

//    public List<MessageResponse> getConversations(Integer userId) {
//        List<String> keys = messageRepository.findDistinctConversationKeysByUserId(userId);
//        return keys.stream()
//                .map(messageRepository::findTop1ByConversationKeyOrderBySentAtDesc)
//                .filter(m -> !m.isEmpty())
//                .map(m -> messageMapper.toMessageResponse(m.getFirst()))
//                .collect(Collectors.toList());
//    }

    public List<ConversationResponse> getConversations(Integer userId) {
        List<String> keys = messageRepository.findDistinctConversationKeysByUserId(userId);
        User currentUser = userRepository.findById(userId).orElseThrow();

        return keys.stream()
                .map(k -> {
                    Message latest = messageRepository.findTopByConversationKeyOrderBySentAtDesc(k);
                    if (latest == null) return null;

                    User partner = latest.getSender().getUserId().equals(userId)
                            ? latest.getReceiver()
                            : latest.getSender();

                    return ConversationResponse.builder()
                            .partnerId(partner.getUserId())
                            .partnerName(partner.getUsername())
                            .lastMessage(latest.getBody())
                            .lastMessageSenderName(latest.getSender().getUsername())
                            .sentAt(latest.getSentAt().toString())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
