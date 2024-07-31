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
import org.springframework.web.client.HttpServerErrorException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/books")
public class BookController {

    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    private final BookService bookService;


    private final UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO){
        try {
            bookService.addBook(bookDTO);
            return ResponseEntity.ok(bookDTO);
        }catch(Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteById(@PathVariable int id){

        try {
            bookService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<BookDTO> updateById(@PathVariable int id, @RequestBody BookDTO updatedBook)
    {
        try {
            Book savedBook=bookService.updateBook(updatedBook, id);
            return ResponseEntity.ok(bookService.bookToBookDTO(savedBook));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable long isbn)
    {
        try {
            return ResponseEntity.ok(bookService.getBookByISBN(isbn));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/getSuggestions")
    public ResponseEntity<List<BookDTO>> getBookSuggestions(@RequestBody BookDTO bookDTO)
    {
        try{
            return ResponseEntity.ok(bookService.searchSuggestedBooks(bookDTO));
        }catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
}
