package com.bryan.libarterbe.DTO;

import java.time.LocalDateTime;

public class MessageDTO {
    String body;
    boolean you;

    LocalDateTime time;

    String username;

    int id;

    public MessageDTO(String body, boolean you, LocalDateTime time, String username, int id) {
        this.body = body;
        this.you = you;
        this.time = time;
        this.username = username;
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isYou() {
        return you;
    }

    public void setYou(boolean you) {
        this.you = you;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
