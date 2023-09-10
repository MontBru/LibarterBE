package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bryan.libarterbe.repository.BookRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }


    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksBySearch(String searchTerm, Pageable pageable)
    {
        return bookRepository.findBookByNameContainingOrDescriptionContaining(searchTerm, searchTerm, pageable);
    }
}
