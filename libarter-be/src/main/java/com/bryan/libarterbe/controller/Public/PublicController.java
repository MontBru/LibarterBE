package com.bryan.libarterbe.controller.Public;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.StorageService;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    StorageService storageService;
    @GetMapping("/")
    public void test()
    {
        storageService.writeResource("img", "Imagine that this is an image");
    }

    @GetMapping("/get")
    public String testGet()
    {
        return storageService.readResource("img");
    }


}
