package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

public record ConversationDTO(int id,
                              String bookName,
                              String clientName,
                              String base64image,
                              MessageDTO lastMessage) {
}
