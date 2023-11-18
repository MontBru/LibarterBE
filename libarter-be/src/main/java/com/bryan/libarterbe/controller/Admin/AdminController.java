package com.bryan.libarterbe.controller.Admin;

import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String helloAdminController(){
        return "Admin level access";
    }

    @DeleteMapping("/deleteAllUsers")
    public String deleteAllUsers(){
        if(userService.deleteAllUsers() == true)
            return "All users have been deleted";
        else
            return "Didn't delete all users";
    }


}
