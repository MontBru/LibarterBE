package com.bryan.libarterbe.DTO;

public class RegistrationDTO {
    //DTO - data transfer object
    private String username;
    private String email;
    private String password;

    private String phoneNumber;

    public RegistrationDTO(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Registration info: " +
                "username: '" + username + '\'' +
                ", password: '" + password + '\''+
                ", email: '" + email +'\'';
    }
}
