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

    private List<String> photos;

    private boolean acceptsTrade;

    private boolean isNew;
    private double price;
    private int userId;

    private long isbn;

    private List<String> tags;

    public static BookDTO bookToBookDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getPrice(),
                book.getUser().getId(),
                book.getPhotos(),
                book.isAcceptsTrade(),
                book.isNew(),
                book.getIsbn(),
                book.getTags());
    }

    public static List<BookDTO> booklistToBookDTOlist(List<Book> books)
    {
        return books.stream()
            .map(BookDTO::bookToBookDTO)
            .collect(Collectors.toList());
    }

    public BookDTO(
            int id,String name,
            String author,
            String description,
            double price,
            int userId,
            List<String> photos,
            boolean acceptsTrade,
            boolean isNew,
            long isbn,
            List<String> tags
    ) {
        this.id = id;
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
