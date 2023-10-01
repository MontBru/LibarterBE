package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.bryan.libarterbe.repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    public byte[] downloadImageAsBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        try (InputStream in = url.openStream()) {
            byte[] imageBytes = StreamUtils.copyToByteArray(in);
            return imageBytes;
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> getBooksBySearch(String searchTerm, Pageable pageable)
    {
        return bookRepository.findBookByNameContainingOrDescriptionContaining(searchTerm, searchTerm, pageable);
    }
}
