package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findById(int id);

    Optional<ApplicationUser> findByEmail(String email);
}
