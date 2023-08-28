package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bryan.libarterbe.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/book")
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO){
        try {
            Book book = new Book(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getDescription(), userService.getUserById(bookDTO.getUserId()));
            bookService.saveBook(book);
            return ResponseEntity.ok(bookDTO);
        }catch(Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

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
    @GetMapping("/getAll")
    @Transactional
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(BookDTO::bookToBookDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOs);
    }

    @DeleteMapping("deleteById/{id}")
    @Transactional
    public ResponseEntity<String> deleteById(@PathVariable int id){
        bookService.deleteById(id);
        return ResponseEntity.ok("Book deleted");
    }

    @PutMapping("updateById/{id}")
    @Transactional
    public ResponseEntity<Book> updateById(@PathVariable int id, @RequestBody Book updatedBook)
    {
        Optional<Book> existingBookOptional = bookService.getBookById(id);

        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setName(updatedBook.getName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());

            Book savedBook = bookService.saveBook(existingBook);

            return ResponseEntity.ok(savedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
