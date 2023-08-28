package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;

public class BookDTO {
    private String name;
    private String author;
    private String description;

    private int userId;

    public static BookDTO bookToBookDTO(Book book)
    {
        return new BookDTO(book.getName(),book.getAuthor(),book.getDescription(), book.getUser().getId());
    }

    public BookDTO(String name, String author, String description, int userId) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.userId = userId;
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
}
