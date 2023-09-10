package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private String email;

    private String username;

    private String phoneNumber;

    private List<Integer> books = new ArrayList<Integer>();

    public static UserDTO UserToUserDTO(ApplicationUser user)
    {
        return new UserDTO(user.getEmail(), user.getUsername(), user.getPhoneNumber() , user.getBooks().stream()
                .map( book -> {return book.getId();} )
                .collect(Collectors.toList()));
    }

    public UserDTO(String email, String username, String phoneNumber, List<Integer> books) {
        this.email = email;
        this.username = username;
        this.books = books;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getBooks() {
        return books;
    }

    public void setBooks(List<Integer> books) {
        this.books = books;
    }
}
