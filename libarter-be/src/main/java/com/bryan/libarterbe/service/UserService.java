package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.repository.RoleRepository;
import com.bryan.libarterbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("couldn't find user with this username"));
    }

    public ApplicationUser getUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("couldn't find user with this username"));
    }

    public ApplicationUser getUserByEmail(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with this email"));
    }

    public ApplicationUser getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteAllUsers()
    {
        try {
            userRepository.deleteAll();
            roleRepository.deleteAll();
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
}
