package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.PasswordResetToken;
import com.bryan.libarterbe.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(ApplicationUser user);
}
