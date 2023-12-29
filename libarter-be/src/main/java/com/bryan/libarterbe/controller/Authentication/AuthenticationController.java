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

    private boolean isPhoneNumberValid(String phoneNumber)
    {
        String regexPattern = "^(\\+359|0)\\d{9}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO body){
        try {
            if (isPhoneNumberValid(body.getPhoneNumber())) {
                authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail(), body.getPhoneNumber());
                return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
            } else
                return ResponseEntity.internalServerError().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();//user exists
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO body){
        try {
            return ResponseEntity.ok(authenticationService.loginUser(body.getUsername(), body.getPassword()));
        }catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest emailReq)
    {
        String email = emailReq.getEmail();
        System.out.println(email);
        try {
            authenticationService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        boolean res = authenticationService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword());
        if (res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
