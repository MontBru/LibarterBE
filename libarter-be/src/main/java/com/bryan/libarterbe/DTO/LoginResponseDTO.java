package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;

public class LoginResponseDTO {
    private int uid;
    private String jwt;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(int uid, String jwt) {
        this.uid = uid;
        this.jwt = jwt;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
