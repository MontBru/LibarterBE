package com.bryan.libarterbe.controller.Admin;

import com.bryan.libarterbe.repository.BookRepository;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    public AdminController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    private final BookRepository bookRepository;


    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable int id)
    {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
