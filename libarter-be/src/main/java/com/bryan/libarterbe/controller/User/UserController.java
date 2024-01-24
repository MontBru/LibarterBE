package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.TokenService;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<String> checkAuthorization()
    {
        return ResponseEntity.ok().build();
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

    @GetMapping("/getLoggedUser")
    @Transactional
    public ResponseEntity<UserDTO> getLoggedUser(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int uid = Math.toIntExact(jwt.getClaim("uid"));

        ApplicationUser user = userService.getUserById(uid);
        if(user != null)
        {
            return ResponseEntity.ok(UserDTO.UserToUserDTO(user));
        }
        else
        {
            return ResponseEntity.internalServerError().build();
        }


    }

    @GetMapping("/getAllBooksByUID/{isRequest}")
    @Transactional
    public ResponseEntity<List<BookDTO>> getAllBooksByUID(@PathVariable boolean isRequest)
    {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int uid = Math.toIntExact(jwt.getClaim("uid"));

        try {
            ApplicationUser user = userService.getUserById(uid);
            if(user == null)
            {
                return ResponseEntity.internalServerError().build();
            }

            List<Book> books = user.getBooks();

            books = books
                    .stream()
                    .filter((Book b)->b.getIsRequest()==isRequest)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(bookService.booklistToBookDTOlist(books));
        } catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }
}
