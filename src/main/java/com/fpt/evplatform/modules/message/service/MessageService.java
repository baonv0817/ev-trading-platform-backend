package com.fpt.evplatform.modules.message.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.message.dto.request.MessageRequest;
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
}
