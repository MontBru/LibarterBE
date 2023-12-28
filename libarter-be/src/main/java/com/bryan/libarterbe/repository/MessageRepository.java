package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    Page<Message> getMessagesByConversation_Id(int conversationId, Pageable pageable);
}
