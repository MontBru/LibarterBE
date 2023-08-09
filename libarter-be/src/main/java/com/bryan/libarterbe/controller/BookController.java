package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bryan.libarterbe.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody Book book){
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Book> getById(@PathVariable int id) {
        Optional<Book> bookOptional = bookService.getBookById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return ResponseEntity.ok(book); // Return 200 OK with the book entity
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id){
        bookService.deleteById(id);
        return ResponseEntity.ok("Book deleted");
    }

    @PutMapping("updateById/{id}")
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
