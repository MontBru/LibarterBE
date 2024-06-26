package com.bryan.libarterbe.controller.Public;

import com.bryan.libarterbe.DTO.UserDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/users")
public class PublicUserController{

    public PublicUserController(UserService userService) {
        this.userService = userService;
    }

    UserService userService;

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<UserDTO> getUserByUID(@PathVariable int id) {
        ApplicationUser user = userService.getUserById(id);
        if(user!=null)
            return ResponseEntity.ok(userService.UserToUserDTO(user));
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
