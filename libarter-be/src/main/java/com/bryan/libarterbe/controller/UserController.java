package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ApplicationUser> getAllUsers(){
        return userService.getAllUsers();
    }
}
