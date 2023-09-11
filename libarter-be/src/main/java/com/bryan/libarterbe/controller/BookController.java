package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.BookPageDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/book")
@CrossOrigin("*")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO){
        try {
            Book book = new Book(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getDescription(), bookDTO.getPrice(), userService.getUserById(bookDTO.getUserId()));
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
        List<BookDTO> bookDTOs = BookDTO.booklistToBookDTOlist(books);
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
    public ResponseEntity<BookDTO> updateById(@PathVariable int id, @RequestBody Book updatedBook)
    {
        Optional<Book> existingBookOptional = bookService.getBookById(id);

        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setName(updatedBook.getName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());

            Book savedBook = bookService.saveBook(existingBook);

            return ResponseEntity.ok(BookDTO.bookToBookDTO(savedBook));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getBooksBySearch")
    @Transactional
    public ResponseEntity<BookPageDTO> getBooksBySearch(@RequestBody SearchBooksDTO body)
    {
        Pageable pageable = PageRequest.of(body.getPageNum(), 20);

        Page<Book> bookPage = bookService.getBooksBySearch(body.getSearchTerm(), pageable);

        List<BookDTO> bookDTOList = BookDTO.booklistToBookDTOlist(bookPage.getContent());


        return ResponseEntity.ok(new BookPageDTO(bookDTOList, bookPage.getTotalPages()));
    }
}
