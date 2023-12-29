package com.bryan.libarterbe.DTO;

import java.util.List;

public class MessagePageDTO {
    private int totalPageCount;
    private List<MessageDTO> messages;

    public MessagePageDTO(int totalPageCount, List<MessageDTO> messages) {
        this.totalPageCount = totalPageCount;
        this.messages = messages;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
