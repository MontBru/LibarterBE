package com.bryan.libarterbe.service;

import com.bryan.libarterbe.configuration.FrontendEndpoint;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.PasswordResetToken;
import com.bryan.libarterbe.model.RegisterToken;
import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.repository.PasswordResetTokenRepository;
import com.bryan.libarterbe.repository.RegisterTokenRepository;
import com.bryan.libarterbe.repository.RoleRepository;
import com.bryan.libarterbe.repository.UserRepository;
import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService, UserService userService, EmailService emailService, PasswordResetTokenRepository resetTokenRepository, RegisterTokenRepository registerTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.emailService = emailService;
        this.resetTokenRepository = resetTokenRepository;
        this.registerTokenRepository = registerTokenRepository;
    }

    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;


    private final TokenService tokenService;


    private final UserService userService;


    private final EmailService emailService;


    private final PasswordResetTokenRepository resetTokenRepository;


    private final RegisterTokenRepository registerTokenRepository;

    private boolean isPhoneNumberValid(String phoneNumber)
    {
        String regexPattern = "^(\\+359|0)\\d{9}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private String generateRegisterToken(String email) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);

        RegisterToken registerToken = new RegisterToken();
        registerToken.setToken(uuid.toString());
        registerToken.setExpiryDateTime(expiryDateTime);
        registerToken.setEmail(email);

        registerTokenRepository.save(registerToken);

        return FrontendEndpoint.endpoint + "/register-token/"+registerToken.getToken();
    }

    public void requestRegister(String email)
    {
        ApplicationUser user;
        try {
            user = userService.getUserByEmail(email);
        }catch (UsernameNotFoundException e){
            user = null;
        }
        if (user != null)
            return;
        String registerLink = generateRegisterToken(email);
        String msgText = "Hello,\nDo you want to register to Libarter? If so click the link:" + registerLink + "\n\n\nRegards,\nLibarter";
        emailService.sendEmail(email, "Register to Libarter", msgText);
    }


    public ApplicationUser registerUser(String username, String password, String phoneNumber, String token){
        RegisterToken registerToken = registerTokenRepository.findByToken(token);
        if(registerToken == null || !tokenNotExpired(registerToken.getExpiryDateTime()))
            return null;


        if (!isPhoneNumberValid(phoneNumber))
            return null;

        String email = registerToken.getEmail();

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        encodedPassword = "{bcrypt}"+encodedPassword;

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(new ApplicationUser(0, encodedPassword, email, username, phoneNumber, authorities));
    }

    public String loginUser(String username, String password) throws AuthenticationException{
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        ApplicationUser user = userService.getUserByUsername(username);
        return tokenService.generateJwt(auth, user.getId());
    }

    private String generateResetToken(ApplicationUser user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);

        PasswordResetToken existingToken = resetTokenRepository.findByUser(user);
        if(existingToken != null)
        {
            resetToken.setId(existingToken.getId());
        }
        resetTokenRepository.save(resetToken);

        return FrontendEndpoint.endpoint + "/reset-password/"+resetToken.getToken();
    }

    public void forgotPassword(String email) throws Exception {
        ApplicationUser user = userService.getUserByEmail(email);
        String resetLink = generateResetToken(user);
        String msgText = "Hello,\nDo you want to reset your password for Libarter? If so click the link:" + resetLink + "\n\n\nRegards,\nLibarter";
        emailService.sendEmail(email, "Password Reset", msgText);
    }

    private boolean tokenNotExpired(LocalDateTime time)
    {
        return time.isAfter(LocalDateTime.now());
    }

    public boolean resetPassword(String token, String password)
    {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token);
        if(resetToken != null && tokenNotExpired(resetToken.getExpiryDateTime()))
        {
            ApplicationUser user = resetToken.getUser();
            user.setPassword("{bcrypt}" + passwordEncoder.encode(password));
            userRepository.save(user);

            resetTokenRepository.delete(resetToken);
            return true;
        }
        return false;
    }
}
