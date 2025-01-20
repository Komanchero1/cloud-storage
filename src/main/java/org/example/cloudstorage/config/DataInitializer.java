package org.example.cloudstorage.config;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Проверяется, существует ли пользователь с заданным логином
        String defaultLogin = "comanch@mail.ru"; // Задается логин
        String defaultPassword = "1234567"; // Задается пароль

        if (!userRepository.findByLogin(defaultLogin).isPresent()) {
            // Если пользователь не существует, создается новый
            User user = new User();
            user.setLogin(defaultLogin);
            user.setPasswordHash(passwordEncoder.encode(defaultPassword)); // Хешируется пароль пароль
            userRepository.save(user);
            System.out.println("Пользователь " + defaultLogin + " был создан.");
        } else {
            System.out.println("Пользователь " + defaultLogin + " уже существует.");
        }
    }
}
