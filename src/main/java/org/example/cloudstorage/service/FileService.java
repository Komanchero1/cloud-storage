package org.example.cloudstorage.service;


import org.example.cloudstorage.exeption.FileNotFoundException;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.repository.FileRepository;
import org.example.cloudstorage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    private final String uploadDir = "uploads/";

    public void uploadFile(String authToken, String filename, MultipartFile file) {
        User user = validateToken(authToken);

        try {
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir + filename);
            file.transferTo(filePath.toFile());

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(filename);
            fileEntity.setSize(file.getSize());
            fileEntity.setFilePath(filePath.toString());
            fileEntity.setUser(user);
            fileRepository.save(fileEntity);
            logger.info("Файл{} успешно загружен {}", filename, user.getLogin());
        } catch (IOException e) {
            logger.error("шибка при загрузке файла {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    public void deleteFile(String authToken, String filename) {
        User user = validateToken(authToken);
        FileEntity fileEntity = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Файл не найден для удаления: {}", filename);
                    return new FileNotFoundException("Файл не найден");
                });

        new File(fileEntity.getFilePath()).delete();
        fileRepository.delete(fileEntity);
        logger.info("Файл {} успешно удален пользователем {}", filename, user.getLogin());
    }

    public byte[] downloadFile(String authToken, String filename) {
        User user = validateToken(authToken);
        FileEntity fileEntity = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Файл не найден для скачивания: {}", filename);
                    return new FileNotFoundException("Файл не найден");
                });

        try {
            return Files.readAllBytes(Paths.get(fileEntity.getFilePath()));
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    public List<FileEntity> listFiles(String authToken) {
        User user = validateToken(authToken);
        return fileRepository.findByUserId(user.getId());
    }

    private User validateToken(String authToken) {
        return userRepository.findByAuthToken(authToken)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Попытка несанкционированного доступа с помощью токена: {}", authToken);
                    return new RuntimeException("Неавторизованный доступ");
                });
    }
}