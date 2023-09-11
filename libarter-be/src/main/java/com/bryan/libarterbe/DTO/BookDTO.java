package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookDTO {
    private int id;
    private String name;
    private String author;
    private String description;

    private double price;
    private int userId;

    public static BookDTO bookToBookDTO(Book book)
    {
        return new BookDTO(book.getId(), book.getName(),book.getAuthor(),book.getDescription(), book.getPrice(), book.getUser().getId());
    }

    public static List<BookDTO> booklistToBookDTOlist(List<Book> books)
    {
        return books.stream()
            .map(BookDTO::bookToBookDTO)
            .collect(Collectors.toList());
    }

    public BookDTO(int id,String name, String author, String description, double price, int userId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
