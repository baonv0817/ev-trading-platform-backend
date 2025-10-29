package com.fpt.evplatform.modules.message.repository;

import com.fpt.evplatform.modules.message.entity.Message;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByConversationKeyOrderBySentAtAsc(String conversationKey);
    List<Message> findBySenderOrReceiverOrderBySentAtDesc(User sender, User receiver);

    List<Message> findTop1ByConversationKeyOrderBySentAtDesc(String conversationKey);

    @Query("SELECT DISTINCT m.conversationKey FROM Message m WHERE m.sender.userId = :userId OR m.receiver.userId = :userId")
    List<String> findDistinctConversationKeysByUserId(@Param("userId") Integer userId);

    Message findTopByConversationKeyOrderBySentAtDesc(String conversationKey);


}
