package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken,Integer> {
    RegisterToken findByToken(String token);
}
