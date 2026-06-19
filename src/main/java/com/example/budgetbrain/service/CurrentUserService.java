package com.example.budgetbrain.service;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser getUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User is not logged in");
        }
        return userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));
    }
}
