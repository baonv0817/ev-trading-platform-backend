package com.fpt.evplatform.modules.message.mapper;

import com.fpt.evplatform.modules.message.dto.request.MessageRequest;
import com.fpt.evplatform.modules.message.dto.response.MessageResponse;
import com.fpt.evplatform.modules.message.entity.Message;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "messageId", ignore = true)
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "receiver", source = "receiver")
    @Mapping(target = "sentAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "conversationKey",
            expression = "java(generateConversationKey(sender.getUserId(), receiver.getUserId()))")
    Message toMessage(MessageRequest request, User sender, User receiver);

    @Mapping(target = "senderId", source = "sender.userId")
    @Mapping(target = "senderName", expression = "java(message.getSender().getUsername())")
    @Mapping(target = "receiverId", source = "receiver.userId")
    @Mapping(target = "receiverName", expression = "java(message.getReceiver().getUsername())")
    MessageResponse toMessageResponse(Message message);

    default String generateConversationKey(Integer senderId, Integer receiverId) {
        return senderId < receiverId
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;
    }
}
