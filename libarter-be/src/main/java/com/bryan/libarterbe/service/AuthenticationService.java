package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.LoginResponseDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.repository.RoleRepository;
import com.bryan.libarterbe.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public ApplicationUser registerUser(String username, String password, String email){
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        encodedPassword = "{bcrypt}"+encodedPassword;

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(new ApplicationUser(0, encodedPassword, username, email, authorities));
    }

    private void setLoginCookie(HttpServletResponse response, String token)
    {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Requires HTTPS
        jwtCookie.setPath("/"); // Set the path appropriately
        jwtCookie.setMaxAge(10000);
        response.addCookie(jwtCookie);


    }

    public LoginResponseDTO loginUser(String username, String password, HttpServletResponse response){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            setLoginCookie(response, token);

            return new LoginResponseDTO(userRepository.findByUsername(username).get().getId(), token);

        }catch (AuthenticationException e){
            throw e;
        }

    }
}
