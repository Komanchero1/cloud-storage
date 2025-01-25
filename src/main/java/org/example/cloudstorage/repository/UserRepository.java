package org.example.cloudstorage.repository;

import org.example.cloudstorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //этот интерфейс является репозиторием, который управляет доступом к данным

//расширяет JpaRepository для работы с сущностью User и её идентификатором типа Long
public interface UserRepository extends JpaRepository<User, Long> {

    // Метод для поиска пользователя по логину
    Optional<User> findByLogin(String login);

    // Метод для поиска пользователя по токену аутентификации
    Optional<User> findByAuthToken(String authToken);
}

