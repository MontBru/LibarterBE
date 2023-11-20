package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.*;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.UserService;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO){
        try {
            bookService.addBook(bookDTO);
            return ResponseEntity.ok(bookDTO);
        }catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<BookDTO> updateById(@PathVariable int id, @RequestBody BookDTO updatedBook)
    {
        try {
            Book savedBook=bookService.updateBook(updatedBook, id);
            return ResponseEntity.ok(BookDTO.bookToBookDTO(savedBook));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getBookByISBN/{isbn}")
    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable long isbn)
    {
        try {
            return ResponseEntity.ok(bookService.getBookByISBN(isbn));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/getBookSuggestions")
    public ResponseEntity<List<BookDTO>> getBookSuggestions(@RequestBody BookDTO bookDTO)
    {
        try{
            return ResponseEntity.ok(bookService.searchSuggestedBooks(bookDTO));
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
