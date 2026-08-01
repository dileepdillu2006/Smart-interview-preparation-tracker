package com.dileep.interview_tracker.service;

import com.dileep.interview_tracker.entity.User;
import com.dileep.interview_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Basic reliability check: don't allow duplicate emails
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

   @Autowired
private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

public User updateUser(Long id, User updatedUser) {
    User existingUser = getUserById(id);
    existingUser.setName(updatedUser.getName());
    existingUser.setEmail(updatedUser.getEmail());

    // Only update password if a real new one was provided — never overwrite with blank
    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    if (updatedUser.getResumeText() != null) {
        existingUser.setResumeText(updatedUser.getResumeText());
    }

    return userRepository.save(existingUser);
}
    public void deleteUser(Long id) {
        User user = getUserById(id); // throws if not found, so we never try to delete a ghost
        userRepository.delete(user);
    }
}