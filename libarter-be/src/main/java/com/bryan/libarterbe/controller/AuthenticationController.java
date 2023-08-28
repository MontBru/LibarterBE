package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.DTO.LoginDTO;
import com.bryan.libarterbe.DTO.LoginResponseDTO;
import com.bryan.libarterbe.DTO.RegistrationDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body){
        try {
            return authenticationService.loginUser(body.getUsername(), body.getPassword());
        }catch (Exception e)
        {
            throw e;
        }
    }

}
