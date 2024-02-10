package com.bryan.libarterbe.controller.Authentication;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.bryan.libarterbe.DTO.EmailRequest;
import com.bryan.libarterbe.DTO.LoginDTO;
import com.bryan.libarterbe.DTO.RegistrationDTO;
import com.bryan.libarterbe.DTO.ResetPasswordDTO;
import com.bryan.libarterbe.model.ApplicationUser;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO body){
        try {
            ApplicationUser response = authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail(), body.getPhoneNumber());
            if(response != null)
                return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
            else
                return ResponseEntity.internalServerError().body("Phone number not valid");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("User exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO body){
        try {
            return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
        }catch (Exception e)
        {
            return ResponseEntity.internalServerError().body("Invalid credentials");
        }
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest emailReq)
    {
        String email = emailReq.getEmail();
        try {
            authenticationService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        boolean res = authenticationService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword());
        if (res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("The time to change the password has expired, try again!");
    }
}
