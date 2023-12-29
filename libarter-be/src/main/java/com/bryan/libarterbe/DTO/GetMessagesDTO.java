package com.bryan.libarterbe.DTO;

public class GetMessagesDTO {
    int conversationId;
    int pageNum;

    public GetMessagesDTO(int conversationId, int pageNum) {
        this.conversationId = conversationId;
        this.pageNum = pageNum;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
