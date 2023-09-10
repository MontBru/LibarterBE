package com.bryan.libarterbe.DTO;

import java.util.List;

public class BookPageDTO {
    private List<BookDTO> books;
    private int totalPageCount;

    public BookPageDTO(List<BookDTO> books, int totalPageCount) {
        this.books = books;
        this.totalPageCount = totalPageCount;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
