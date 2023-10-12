package com.bryan.libarterbe.DTO;

import java.util.List;

public class BookInfoDTO {
    private String name;
    private String author;
    private String description;
    private double price;

    private String publisher;

    private String language;
    private int yearPublished;
    private long isbn;


    public BookInfoDTO() {
    }

    public BookInfoDTO(String name, String author, String description, double price, String publisher, String language, int yearPublished, long isbn) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.publisher = publisher;
        this.language = language;
        this.yearPublished = yearPublished;
        this.isbn = isbn;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }
}
