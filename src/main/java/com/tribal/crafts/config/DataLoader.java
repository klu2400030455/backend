package com.tribal.crafts.config;

import com.tribal.crafts.entity.Role;
import com.tribal.crafts.entity.User;
import com.tribal.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin already exists
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ADMIN);
            
            userRepository.save(admin);
            System.out.println("✓ Admin user seeded successfully: admin@gmail.com / 123456");
        } else {
            System.out.println("✓ Admin user already exists");
        }
    }
}
