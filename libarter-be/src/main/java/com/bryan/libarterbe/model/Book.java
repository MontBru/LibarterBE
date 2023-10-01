package com.bryan.libarterbe.model;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.service.UserService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private String description;

    private List<String> photos;

    private boolean acceptsTrade;

    private boolean isNew;

    private double price;

    private long isbn;

    private List<String> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private ApplicationUser user;

    public Book(
            String name,
            String author,
            String description,
            double price,
            ApplicationUser user,
            List<String> photos,
            boolean acceptsTrade,
            boolean isNew,
            long isbn,
            List<String> tags
    ) throws Exception {
        if(photos.size()>5)
            throw new Exception("can't add this many photos");
        if(tags.size()>10)
            throw new Exception("can't add this many tags");
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.user = user;
        this.photos = photos;
        this.acceptsTrade = acceptsTrade;
        this.isNew = isNew;
        this.tags = tags;
        this.isbn = isbn;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) throws Exception {
        if(tags.size()>10)
            throw new Exception();
        this.tags = tags;
    }

    public Book() {
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) throws Exception {
        if(photos.size()>5)
            throw new Exception();
        this.photos = photos;
    }

    public boolean isAcceptsTrade() {
        return acceptsTrade;
    }

    public void setAcceptsTrade(boolean acceptsTrade) {
        this.acceptsTrade = acceptsTrade;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
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
