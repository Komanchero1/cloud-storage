package org.example.cloudstorage;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.example.cloudstorage.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthorizationServiceTest {

    @Mock //заглушка для UserRepository
    private UserRepository userRepository;

    @Mock //заглушка для PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @InjectMocks // внедряет моки в экземпляр AuthorizationService
    private AuthorizationService authorizationService;

    @BeforeEach //выполняется перед каждым тестом
    public void setUp() {
        MockitoAnnotations.openMocks(this);// инициализация моков перед выполнением тестов
    }


    // тест для проверки успешной аутентификации с корректными учетными данными
    @Test
    public void testAuthenticateValidCredentialsReturnsToken() {
        User user = new User();// создание нового объекта User
        user.setLogin("comanch@mail.ru");//установка логина
        user.setPasswordHash("hashedPassword");//установка  хэша пароля

        // настройка поведения мока, когда вызывается findByLogin с указанным логином,
        // возвращать созданного пользователя
        when(userRepository.findByLogin("comanch@mail.ru")).thenReturn(Optional.of(user));

        // настройка поведения мока ,когда вызывается matches с указанным паролем и хэшом, возвращать true
        when(passwordEncoder.matches("1234567", "hashedPassword")).thenReturn(true);
        // вызов метода аутентификации и получение токена
        String token = authorizationService.authenticate("comanch@mail.ru", "1234567");
        // проверка, что возвращаемый токен равен токену пользователя
        assertEquals(token, user.getAuthToken());
        verify(userRepository).save(user);// проверка, что метод save был вызван на userRepository
    }


    // тест для проверки обработки невалидных учетных данных
    @Test
    public void testAuthenticateInvalidCredentialsThrowsException() {
        // настройка поведения мока , когда вызывается findByLogin с указанным логином,
        // возвращать пустой Optional
        when(userRepository.findByLogin("comanch@mail.ru")).thenReturn(Optional.empty());

        // при вызове authenticate с неверным паролем должно выбрасывается исключение RuntimeException
        assertThrows(RuntimeException.class, () -> {
            authorizationService.authenticate("comanch@mail.ru", "wrongPassword");
        });
    }
}