package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String helloUserController(){
        return "User access level";
    }

    @GetMapping("/getAllUsers")
    @Transactional
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers().stream()
                .map(user ->{
                    return UserDTO.UserToUserDTO(user);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getAllBooksByUID/{id}/{isRequest}")
    @Transactional
    public ResponseEntity<List<BookDTO>> getAllBooksByUID(@PathVariable int id, @PathVariable boolean isRequest)
    {
        try {
            List<Book> books = userService.getUserById(id).getBooks();
            books = books
                    .stream()
                    .filter((Book b)->b.getIsRequest()==isRequest)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(BookDTO.booklistToBookDTOlist(books));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getUser/{id}")
    @Transactional
    public ResponseEntity<UserDTO> getUserByUID(@PathVariable int id) {
        try {
            return ResponseEntity.ok(UserDTO.UserToUserDTO(userService.getUserById(id)));
        } catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }
}
