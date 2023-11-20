package com.bryan.libarterbe.controller.Public;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.BookPageDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/public/book")
public class PublicBookController{
    @Autowired
    BookService bookService;

    @GetMapping("/getById/{id}")
    @Transactional
    public ResponseEntity<BookDTO> getById(@PathVariable int id) {
        Optional<Book> bookOptional = bookService.getBookById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            BookDTO bookDTO = BookDTO.bookToBookDTO(book);
            return ResponseEntity.ok(bookDTO); // Return 200 OK with the book entity
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

    @PostMapping("/getBooksBySearch")
    @Transactional
    public ResponseEntity<BookPageDTO> getBooksBySearch(@RequestBody SearchBooksDTO body)
    {
        return bookService.searchBooks(body, 1, body.isRequest());
    }

    @PostMapping("/getBooksByAuthorSearch")
    @Transactional
    public ResponseEntity<BookPageDTO> getBooksByAuthorSearch(@RequestBody SearchBooksDTO body)
    {
        return bookService.searchBooks(body, 2, body.isRequest());
    }

    @PostMapping("/getBooksByTagSearch")
    @Transactional
    public ResponseEntity<BookPageDTO> getBooksByTagSearch(@RequestBody SearchBooksDTO body)
    {
        return bookService.searchBooks(body, 3, body.isRequest());
    }
}
