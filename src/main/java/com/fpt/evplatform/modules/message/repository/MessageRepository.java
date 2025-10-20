package com.fpt.evplatform.modules.message.repository;

import com.fpt.evplatform.modules.message.entity.Message;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByConversationKeyOrderBySentAtAsc(String conversationKey);
    List<Message> findBySenderOrReceiverOrderBySentAtDesc(User sender, User receiver);
}
