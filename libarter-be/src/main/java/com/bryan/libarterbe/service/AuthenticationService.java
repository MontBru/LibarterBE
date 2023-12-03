package com.bryan.libarterbe.service;

import com.bryan.libarterbe.configuration.FrontendEndpoint;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.PasswordResetToken;
import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.repository.PasswordResetTokenRepository;
import com.bryan.libarterbe.repository.RoleRepository;
import com.bryan.libarterbe.repository.UserRepository;
import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

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

    private String generateResetToken(ApplicationUser user) throws Exception {
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
        PasswordResetToken token = resetTokenRepository.save(resetToken);

        if(token != null)
        {
            String url = FrontendEndpoint.endpoint + "/reset-password/"+resetToken.getToken();
            return url;
        }
        else
            throw new Exception("Couldn't generate token");
    }

    public void forgotPassword(String email) throws Exception {
        Optional<ApplicationUser> userOptional = userRepository.findByEmail(email);
        ApplicationUser user;
        if(userOptional.isPresent())
            user = userOptional.get();
        else
            throw new Exception("User not found");

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
