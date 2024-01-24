package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDTO {
    private int id;

    private boolean isRequest;
    private String name;
    private String author;
    private String description;

    private List<String> photos;

    private boolean acceptsTrade;

    private boolean isNew;
    private double price;
    private int userId;
    private long isbn;

    private List<String> tags;

    private String publisher;

    private String language;
    private int yearPublished;

    public BookDTO(
            int id,
            boolean isRequest,
            String name,
            String author,
            String description,
            double price,
            int userId,
            List<String> photos,
            boolean acceptsTrade,
            boolean isNew,
            long isbn,
            List<String> tags,
            String publisher,
            String language,
            int yearPublished
    ) {
        this.id = id;
        this.isRequest = isRequest;
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.photos = photos;
        this.acceptsTrade = acceptsTrade;
        this.isNew = isNew;
        this.tags = tags;
        this.isbn = isbn;
        this.publisher = publisher;
        this.language = language;
        this.yearPublished = yearPublished;
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
