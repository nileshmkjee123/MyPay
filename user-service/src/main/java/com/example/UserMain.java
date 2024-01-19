package com.example;

import com.example.models.User;
import com.example.repo.UserRepository;
import com.example.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class UserMain implements CommandLineRunner {
    private UserRepository userRepository;
    public static void main(String[] args) {

        SpringApplication.run(UserMain.class);
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(
                User.builder()
                        .name("txn-service")
                        .mobile("txnservice")
                        .authorities(Constants.SERVICE_AUTHORITY)
                        .password(new BCryptPasswordEncoder().encode("P@ss123"))
                        .build()
        );
    }
}