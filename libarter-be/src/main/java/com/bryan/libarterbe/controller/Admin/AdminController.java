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
    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
    public String helloAdminController()
    {
        return "Admin level access";
    }

    @DeleteMapping("/deleteOffer/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable int id)
    {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/deleteAllUsers")
//    public ResponseEntity<String> deleteAllUsers(){
//        if(userService.deleteAllUsers() == true)
//            return ResponseEntity.ok().build();
//        else
//            return ResponseEntity.internalServerError().build();
//    }


}
