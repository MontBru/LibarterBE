package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Conversation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Integer> {
    Optional<Conversation> findConversationByBook_IdAndUser_Id(int bookId, int userId);
    List<Conversation> findConversationsByUser_Id(int userId);

    List<Conversation> findConversationsByBook_User_Id(int bookUserId);
}
