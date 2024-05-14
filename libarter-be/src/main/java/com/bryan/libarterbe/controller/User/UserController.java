package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.SearchBooksDTO;
import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.TokenService;
import com.bryan.libarterbe.service.UserService;
import com.bryan.libarterbe.utils.JwtUtility;
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

    public UserController(UserService userService, TokenService tokenService, BookService bookService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.bookService = bookService;
    }

    private final UserService userService;


    private final TokenService tokenService;


    private final BookService bookService;

    @GetMapping("/")
    public ResponseEntity<String> checkAuthorization()
    {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getLoggedUser")
    @Transactional
    public ResponseEntity<UserDTO> getLoggedUser(){
        int uid = JwtUtility.getUid();

        ApplicationUser user = userService.getUserById(uid);
        if(user != null)
        {
            return ResponseEntity.ok(userService.UserToUserDTO(user));
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
        int uid = JwtUtility.getUid();

        try {
            ApplicationUser user = userService.getUserById(uid);
            if(user == null)
                return ResponseEntity.internalServerError().build();

            List<Book> books = user.getBooks();

            books = bookService.filterBooksByRequest(isRequest, books);

            return ResponseEntity.ok(bookService.booklistToBookDTOlist(books));
        } catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }
}
