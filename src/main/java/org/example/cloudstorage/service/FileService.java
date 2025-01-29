package org.example.cloudstorage.service;


import org.example.cloudstorage.dto.FileDTO;
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
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service //класс является сервисом, который будет управляться контейнером Spring
public class FileService {

    // логгер для записи информации о событиях и ошибках
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired //автоматически создается объект UserRepository
    private UserRepository userRepository;


    @Autowired //автоматически создается объект FileRepository
    private FileRepository fileRepository;


    // метод для загрузки файла
    public void uploadFile(String authToken, String filename, MultipartFile file) {
        User user = validateToken(authToken); // проверка токена и получение пользователя

        // Проверка существования файла с таким именем для данного пользователя
       if (fileRepository.findByFilenameAndUser(filename, user).isPresent()) {
            throw new RuntimeException("Файл с таким именем уже существует."); // выбрасывается исключение, если файл существует
        }

        try {
            // Получение корневой директории проекта
            String projectRoot = System.getProperty("user.dir");
            // Путь к директории загрузки
            String uploadDir = projectRoot + File.separator + "uploads" + File.separator;

            // Создание директории, если она не существует
            Files.createDirectories(Paths.get(uploadDir));

            // Сохранение файла
            File destinationFile = new File(uploadDir + filename);
            file.transferTo(destinationFile); // перемещение загруженного файла в указанную директорию

            // Создание объекта FileEntity
            FileEntity fileEntity = new FileEntity(); // создание объекта FileEntity
            fileEntity.setFilename(filename); // установка имени файла
            fileEntity.setSize(file.getSize()); // установка размера файла
            fileEntity.setFilePath(destinationFile.getAbsolutePath()); // установка пути к файлу
            fileEntity.setUser(user); // установка связи с пользователем

            fileRepository.save(fileEntity); // сохранение информации в базе данных
            logger.info("Файл {} успешно загружен пользователем {}", filename, user.getLogin());

        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }



    // метод для удаления файла
    public void deleteFile(String authToken, String filename) {
        User user = validateToken(authToken);// проверка токена и получение пользователя
        FileEntity fileEntity = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Файл не найден для удаления: {}", filename);
                    return new FileNotFoundException("Файл не найден");//если файл не найден
                });

        new File(fileEntity.getFilePath()).delete();// удаление файла с диска
        fileRepository.delete(fileEntity);// удаление информации о файле из базы данных
        logger.info("Файл {} успешно удален пользователем {}", filename, user.getLogin());
    }


    // метод для скачивания файла
    public byte[] downloadFile(String authToken, String filename) {
        User user = validateToken(authToken);// проверка токена и получение пользователя
        FileEntity fileEntity = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Файл не найден для скачивания: {}", filename);
                    return new FileNotFoundException("Файл не найден");//если файл не найден
                });

        try {
            return Files.readAllBytes(Paths.get(fileEntity.getFilePath()));
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }


    // Метод для получения списка файлов пользователя
    public List<FileDTO> listFiles(String authToken) {
        User user = validateToken(authToken); // проверка токена и получение пользователя
        List<FileEntity> files = fileRepository.findByUserId(user.getId()); // получение файлов
        return files.stream()
                .map(file -> new FileDTO(file.getId(), file.getFilename(), file.getSize())) // преобразование в DTO
                .collect(Collectors.toList()); // сбор в список
    }


    // метод для проверки токена аутентификации
    private User validateToken(String authToken) {
        return userRepository.findByAuthToken(authToken)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Попытка несанкционированного доступа с помощью токена: {}", authToken);
                    return new RuntimeException("Неавторизованный доступ");
                });
    }
}