package org.example.cloudstorage;

import org.example.cloudstorage.config.DataInitializer;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;


public class DataInitializerTest {

    @Mock //заглушка для UserRepository
    private UserRepository userRepository;

    @Mock //заглушка для PasswordEncoder
    private PasswordEncoder passwordEncoder;

    // внедряет моки в экземпляр DataInitializer
    @InjectMocks
    private DataInitializer dataInitializer;

    // метод, который выполняется перед каждым тестом
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);// инициализация моков перед выполнением тестов
    }


    // тест для проверки создания пользователя, если он не существует
    @Test
    public void testRun_UserDoesNotExist_CreatesUser() throws Exception {
        String defaultLogin = "comanch@mail.ru"; // логин по умолчанию
        String defaultPassword = "1234567"; // пароль по умолчанию

        // настройка поведения мока, когда вызывается findByLogin с указанным логином,
        //будет возвращен пустой Optional
        when(userRepository.findByLogin(defaultLogin)).thenReturn(Optional.empty());
        // настройка поведения мока, когда вызывается encode с указанным паролем,
        // возвращать хэшированный пароль
        when(passwordEncoder.encode(defaultPassword)).thenReturn("hashedPassword");
        // вызов метода run для инициализации данных
        dataInitializer.run();
        // проверка, что метод save был вызван на userRepository для сохранения нового пользователя
        verify(userRepository).save(any(User.class));
    }

    // тест для проверки, что пользователь не создается, если он уже существует
    @Test
    public void testRun_UserExists_DoesNotCreateUser() throws Exception {
        String defaultLogin = "comanch@mail.ru"; //логин по умолчанию
        User existingUser = new User(); // создание объекта существующего пользователя
        existingUser.setLogin(defaultLogin);// установка логина для существующего пользователя

        // настройка поведения мока, когда вызывается findByLogin с указанным логином,
        // возвращать существующего пользователя
        when(userRepository.findByLogin(defaultLogin)).thenReturn(Optional.of(existingUser));
        // вызов метода run для инициализации данных
        dataInitializer.run();
        // проверка, что метод save не был вызван на userRepository, так как пользователь уже существует
        verify(userRepository, never()).save(any(User.class));
    }
}