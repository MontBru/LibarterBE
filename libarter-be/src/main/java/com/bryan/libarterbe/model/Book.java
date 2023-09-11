package com.bryan.libarterbe.model;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.service.UserService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private String description;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private ApplicationUser user;

    public Book(String name, String author, String description, double price, ApplicationUser user) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.user = user;
    }

    public Book() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser userId) {
        this.user = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
