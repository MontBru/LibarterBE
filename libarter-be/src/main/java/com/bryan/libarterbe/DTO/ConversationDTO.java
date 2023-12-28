package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

public class ConversationDTO {


    private int id;
    private String bookName;

    private String clientName;

    private String base64image;

    private MessageDTO lastMessage;



    public ConversationDTO(int id, String bookName, String clientName, String base64image, MessageDTO lastMessage) {
        this.id = id;
        this.bookName = bookName;
        this.clientName = clientName;
        this.base64image = base64image;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public MessageDTO getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDTO lastMessage) {
        this.lastMessage = lastMessage;
    }
}
