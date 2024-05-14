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


    public PublicBookController(BookService bookService) {
        this.bookService = bookService;
    }

    BookService bookService;

    @GetMapping("/getById/{id}")
    @Transactional
    public ResponseEntity<BookDTO> getById(@PathVariable int id) {
        BookDTO book = bookService.getBookDTOById(id);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getBooksBySearch")
    @Transactional
    public ResponseEntity<BookPageDTO> getBooksBySearch(@RequestBody SearchBooksDTO body)
    {
        return bookService.searchBooks(body, body.isRequest());
    }
}
