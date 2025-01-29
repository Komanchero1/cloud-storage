package org.example.cloudstorage.config;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component  // класс является компонентом Spring
//при запуске приложения создает логин и пароль пользователя
public class DataInitializer implements CommandLineRunner {

    @Autowired // автоматически создастся объект UserRepository
    private UserRepository userRepository;

    @Autowired // автоматически создастся объект PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @Override
    //метод для создания логина и пароля при запуске приложения
    public void run(String... args) throws Exception {

        String defaultLogin = "comanch@mail.ru"; // Задается логин
        String defaultPassword = "1234567"; // Задается пароль

        // Проверяется, существует ли пользователь с заданным логином
        if (!userRepository.findByLogin(defaultLogin).isPresent()) {
            // Если пользователь не существует, создается новый
            User user = new User();
            user.setLogin(defaultLogin);
            user.setPasswordHash(passwordEncoder.encode(defaultPassword)); // Хешируется пароль
            userRepository.save(user);//сохраняется логин и хеш пароля
            System.out.println("Пользователь " + defaultLogin + " был создан.");
        } else {
            System.out.println("Пользователь " + defaultLogin + " уже существует.");
        }
    }
}
