package com.bryan.libarterbe.model;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.service.UserService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isRequest;
    private String name;
    private String author;
    private String description;

    private List<String> photos;

    private boolean acceptsTrade;

    private boolean isNew;

    private double price;

    private long isbn;

    private String publisher;

    private String language;
    private int yearPublished;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="book_tag_junction",
            joinColumns = {@JoinColumn(name="book_id")},
            inverseJoinColumns = {@JoinColumn(name="tag_id")}
    )
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private ApplicationUser user;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Conversation> conversations = new ArrayList<Conversation>();

    public Book(
            int id,
            boolean isRequest,
            String name,
            String author,
            String description,
            double price,
            ApplicationUser user,
            List<String> photos,
            boolean acceptsTrade,
            boolean isNew,
            long isbn,
            List<Tag> tags,
            String publisher,
            String language,
            int yearPublished
    ) throws Exception {
        if(photos.size()>5)
            throw new Exception("can't add this many photos");
        if(tags.size()>10)
            throw new Exception("can't add this many tags");
        this.id = id;
        this.isRequest = isRequest;
        this.name = name;
        this.author = author;
        if(description.length()>100)
            this.description = description.substring(0,99);
        else
            this.description = description;
        this.price = price;
        this.user = user;
        this.photos = photos;
        this.acceptsTrade = acceptsTrade;
        this.isNew = isNew;
        this.tags = tags;
        this.isbn = isbn;
        this.publisher = publisher;
        this.language = language;
        this.yearPublished = yearPublished;
    }

    public Book(
            boolean isRequest,
            String name,
            String author,
            String description,
            double price,
            ApplicationUser user,
            List<String> photos,
            boolean acceptsTrade,
            boolean isNew,
            long isbn,
            List<Tag> tags,
            String publisher,
            String language,
            int yearPublished
    ) throws Exception {
        if(photos.size()>5)
            throw new Exception("can't add this many photos");
        if(tags.size()>10)
            throw new Exception("can't add this many tags");
        this.isRequest = isRequest;
        this.name = name;
        this.author = author;
        if(description.length()>100)
            this.description = description.substring(0,99);
        else
            this.description = description;
        this.price = price;
        this.user = user;
        this.photos = photos;
        this.acceptsTrade = acceptsTrade;
        this.isNew = isNew;
        this.tags = tags;
        this.isbn = isbn;
        this.publisher = publisher;
        this.language = language;
        this.yearPublished = yearPublished;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) throws Exception {
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

    public boolean getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(boolean request) {
        isRequest = request;
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
        if(description.length()>100)
            this.description = description.substring(0,99);
        else
            this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }
}
