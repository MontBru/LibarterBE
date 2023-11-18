package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.repository.RoleRepository;
import com.bryan.libarterbe.repository.UserRepository;
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

    @Autowired
    private UserService userService;

    public ApplicationUser registerUser(String username, String password, String email, String phoneNumber){
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        encodedPassword = "{bcrypt}"+encodedPassword;

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(new ApplicationUser(0, encodedPassword, email, username, phoneNumber, authorities));
    }

    public String loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            ApplicationUser user = userService.getUserByUsername(username);
            String token = tokenService.generateJwt(auth, user.getId());

            return token;

        }catch (AuthenticationException e){
            throw e;
        }

    }
}
