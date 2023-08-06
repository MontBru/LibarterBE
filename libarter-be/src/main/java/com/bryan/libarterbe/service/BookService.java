package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    public Book saveBook(Book book);
    public Optional<Book> getBookById(int id);
    public List<Book> getAllBooks();

    public void deleteById(int id);
}
