package com.bryan.libarterbe.controller.Authentication;

import com.bryan.libarterbe.DTO.LoginDTO;
import com.bryan.libarterbe.DTO.RegistrationDTO;
import com.bryan.libarterbe.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    private boolean isPhoneNumberValid(String phoneNumber)
    {
        String regexPattern = "^(\\+359|0)\\d{9}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO body){
        if(isPhoneNumberValid( body.getPhoneNumber() )) {
            authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail(), body.getPhoneNumber());
            return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
        }
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO body){
        try {
            return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }
    }

}
