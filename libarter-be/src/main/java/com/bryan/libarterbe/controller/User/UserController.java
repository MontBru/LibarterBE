package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.Book;
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

    @GetMapping("/getAllUsers")
    @Transactional
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers().stream()
                .map(user ->{
                    return UserDTO.UserToUserDTO(user);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getAllBooksByUID/{isRequest}")
    @Transactional
    public ResponseEntity<List<BookDTO>> getAllBooksByUID(@PathVariable boolean isRequest)
    {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            List<Book> books = userService.getUserById(Math.toIntExact(jwt.getClaim("uid"))).getBooks();

            books = books
                    .stream()
                    .filter((Book b)->b.getIsRequest()==isRequest)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(BookDTO.booklistToBookDTOlist(books));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
