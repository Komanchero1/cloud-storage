package org.example.cloudstorage.repository;

import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //интерфейс является репозиторием, который управляет доступом к данным

// расширяет JpaRepository для работы с сущностью FileEntity и её идентификатором типа Long
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    // Метод для поиска файла по имени и пользователю
    Optional<FileEntity> findByFilenameAndUser(String filename, User user);

    // Метод для поиска всех файлов, принадлежащих пользователю по его идентификатору
    List<FileEntity> findByUserId(Long userId);
}
