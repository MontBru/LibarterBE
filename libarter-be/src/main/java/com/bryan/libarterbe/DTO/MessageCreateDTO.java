package com.bryan.libarterbe.DTO;

public class MessageCreateDTO {
    String body;

    int bookId;

    public MessageCreateDTO(String body, int bookId) {
        this.body = body;
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
