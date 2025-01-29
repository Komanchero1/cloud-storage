package org.example.cloudstorage.service;


import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service //класс является сервисом, который будет управляться контейнером Spring
public class AuthorizationService {

    // логгер для записи информации о событиях и ошибках
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired//автоматически создается объект UserRepository
    private UserRepository userRepository;

    @Autowired //автоматически создается объект PasswordEncoder
    private PasswordEncoder passwordEncoder;


    // метод для аутентификации пользователя по логину и паролю
    public String authenticate(String login, String password) {
        // поиск пользователя по логину, если не найден - выбрасывается исключение
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Неверный логин или пароль"));

        // проверка совпадения пароля с хэшом
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        // генерация токена
        String authToken = generateAuthToken(user);
        user.setAuthToken(authToken); // Установка токена
        userRepository.save(user); // Сохранение пользователя с новым токеном
        return authToken; // Возвращаем токен
    }


    // метод для выхода пользователя из системы
    public void logout(String authToken) {
        // поиск пользователя по токену
        User user = userRepository.findByAuthToken(authToken)
                .orElseThrow(() -> {
                    logger.warn("Попытка выхода из системы с неверным токеном: {}", authToken);// логирование
                    return new RuntimeException("Неавторизованный доступ");
                });
        user.setAuthToken(null);//установка токена в null, завершение сессии
        userRepository.save(user);// сохранение обновленного пользователя в базе данных
        logger.info("Пользователь {} \n" + "успешно вышел из системы", user.getLogin());
    }


    // метод для генерации токена аутентификации
    private String generateAuthToken(User user) {
        return java.util.UUID.randomUUID().toString();// Генерация уникального токена
    }
}
