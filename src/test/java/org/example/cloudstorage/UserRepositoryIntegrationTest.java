package org.example.cloudstorage;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.assertj.core.api.Assertions.assertThat;


// это интеграционный тест, загружающий контекст приложения
@SpringBootTest(classes = CloudStorageApplication.class)
@ExtendWith(SpringExtension.class) //позволяет использовать возможности Spring в тестах JUnit 5
@Testcontainers //указывает, что в тестах будут использоваться контейнеры
@AutoConfigureMockMvc  //автоматически настраивает MockMvc для тестирования контроллеров
public class UserRepositoryIntegrationTest {


    // Создание контейнера PostgreSQL для тестирования
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb") //установка имени базы данных для контейнера
            .withUsername("postgres") //установка имени пользователя для подключения к базе данных
            .withPassword("12345"); //установка пароля для подключения к базе данных

    @Autowired //автоматически внедряет зависимость UserRepository
    private UserRepository userRepository;

    @BeforeEach //метод, который выполняется перед каждым тестом
    public void setUp() {
        userRepository.deleteAll(); //очистка всех записей в репозитории перед каждым тестом
    }


    //тест для проверки поиска пользователя по логину, если пользователь существует
    @Test
    public void testFindByLoginUserExists() {
        User user = new User();
        user.setLogin("comanch@mail.ru");//установка логина пользователя
        user.setPasswordHash("hashedPassword"); //установка хэша пароля пользователя
        userRepository.save(user);//сохранение пользователя в базе данных

        //поиск пользователя по логину
        var foundUser = userRepository.findByLogin("comanch@mail.ru");
        //проверка, что пользователь найден
        assertThat(foundUser).isPresent();//проверка, что Optional не пустой
        //проверка, что логин найденного пользователя соответствует ожидаемому
        assertThat(foundUser.get().getLogin()).isEqualTo("comanch@mail.ru");
    }
}

