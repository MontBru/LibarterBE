package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
}
