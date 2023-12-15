package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Integer> {
}
