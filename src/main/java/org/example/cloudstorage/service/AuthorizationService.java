package org.example.cloudstorage.service;


import org.example.cloudstorage.exeption.InvalidCredentialsException;
import org.example.cloudstorage.exeption.UserNotFoundException;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Не удалось выполнить аутентификацию пользователя: {}", login);
                    return new UserNotFoundException("Пользователь не найден");
                });

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            logger.warn("Неверный логин или пароль: {}", login);
            throw new InvalidCredentialsException("Неверный логин или пароль");
        }

        String authToken = generateAuthToken(user);
        user.setAuthToken(authToken);
        userRepository.save(user);
        logger.info("Пользователь {} аутентифицирован успешно", login);
        return authToken;
    }

    public void logout(String authToken) {
        User user = userRepository.findByAuthToken(authToken)
                .orElseThrow(() -> {
                    logger.warn("Попытка выхода из системы с неверным токеном: {}", authToken);
                    return new RuntimeException("Неавторизованный доступ");
                });
        user.setAuthToken(null);
        userRepository.save(user);
        logger.info("Пользователь {} \n" + "успешно вышел из системы", user.getLogin());
    }

    private String generateAuthToken(User user) {
        return java.util.UUID.randomUUID().toString();
    }
}
