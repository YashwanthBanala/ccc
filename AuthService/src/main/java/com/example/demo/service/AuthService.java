package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    public String login(String email, String password) {
//        Optional<User> optUser = userRepository.findByEmail(email);
//        if (optUser.isPresent() && passwordEncoder.matches(password, optUser.get().getPassword())) {
//            return jwtUtil.generateToken(optUser.get());
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
    
    public String login(String email, String password) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            System.out.println("❌ No user found for email: " + email);
            throw new RuntimeException("Invalid credentials");
        }

        User user = optUser.get();
        System.out.println("🔍 Checking password for: " + user.getEmail());
        System.out.println("DB password (hashed): " + user.getPassword());
        System.out.println("Raw password: " + password);

        if (passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("✅ Password matched! Generating token...");
            return jwtUtil.generateToken(user);
        } else {
            System.out.println("❌ Password mismatch!");
            throw new RuntimeException("Invalid credentials");
        }
    }


    public boolean validate(String token) {
        return jwtUtil.validateToken(token);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setRole(updatedUser.getRole());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
