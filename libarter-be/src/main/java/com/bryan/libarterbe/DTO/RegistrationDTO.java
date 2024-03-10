package com.bryan.libarterbe.DTO;

public class RegistrationDTO {
    //DTO - data transfer object
    private String username;
    private String password;

    private String phoneNumber;

    private String token;

    public RegistrationDTO(String username, String password, String phoneNumber, String token) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Registration info: " +
                "username: '" + username + '\'' +
                ", password: '" + password + '\'';
    }
}
