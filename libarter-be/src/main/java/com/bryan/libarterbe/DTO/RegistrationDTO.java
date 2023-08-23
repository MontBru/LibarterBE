package com.bryan.libarterbe.DTO;

public class RegistrationDTO {
    //DTO - data transfer object
    private String username;
    private String password;

    public RegistrationDTO(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "Registration info: " +
                "username: '" + username + '\'' +
                ", password: '" + password + '\'';
    }
}
