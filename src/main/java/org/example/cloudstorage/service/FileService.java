package org.example.cloudstorage.service;


import jakarta.transaction.Transactional;
import org.example.cloudstorage.dto.FileDTO;
import org.example.cloudstorage.exсeption.*;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
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
    public void uploadFile(String authToken, String filename, MultipartFile file) throws FileAlreadyExistsException {
        User user = validateToken(authToken); // проверка токена и получение пользователя

        // Проверка существования файла с таким именем для данного пользователя
       if (fileRepository.findByFilenameAndUser(filename, user).isPresent()) {
            throw new FileAlreadyExistsException("Файл с таким именем уже существует."); // выбрасывается исключение, если файл существует
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
            throw new FileUploadException("Ошибка при загрузке файла", e);
        }
    }


    // метод для удаления файла
    @Transactional //при возникновении ошибки во время работы метода все изменения этой транзакции будут отменены
    public void deleteFile(String authToken, String filename) throws FileNotFoundException {
        User user = validateToken(authToken);// проверка токена и получение пользователя

        //поиск файла в базе данных используя метод репозитория
        FileEntity fileEntity = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Файл не найден для удаления: {}", filename);
                    return new FileNotFoundException("Файл не найден");//если файл не найден
                });

        // сначала удаляется информация о файле из базы данных
        fileRepository.delete(fileEntity);
        // создается объект представляющий файл который нужно удалить и определяется путь к нему
        File fileToDelete = new File(fileEntity.getFilePath());
        if (fileToDelete.delete()) {    //попытка удалить файл с диска возвращает "true или  false"
            logger.info("Файл {} успешно удален пользователем {}", filename, user.getLogin());
        } else {  // если нет то логируется ошибка
            logger.error("Ошибка при удалении файла с диска: {}", filename);
            throw new FileDeletionException("Ошибка при удалении файла с диска");// выбрасывается исключение
        }
    }


    // метод для скачивания файла
    public byte[] downloadFile(String authToken, String filename) throws FileNotFoundException {
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
            throw new FileDownloadException("Ошибка при загрузке файла", e);
        }
    }


    // Метод для получения списка файлов пользователя
    public List<FileDTO> listFiles(String authToken) {
        User user = validateToken(authToken); // проверка токена и получение пользователя
        List<FileEntity> files = fileRepository.findByUserId(user.getId()); // получение файлов
        //проверка пустой список или нет
        if (files.isEmpty()) {
            throw new NoFilesFoundException("Нет файлов для просмотра."); // выбрасываем исключение, если список пуст
        }
        return files.stream()
                .map(file -> new FileDTO(file.getId(), file.getFilename(), file.getSize())) // преобразование в DTO
                .collect(Collectors.toList()); // формирование списка
    }


    // метод для проверки токена аутентификации
    private User validateToken(String authToken) {
        return userRepository.findByAuthToken(authToken)
                .orElseThrow(() -> {
                    logger.warn("\n" +
                            "Попытка несанкционированного доступа с помощью токена: {}", authToken);
                    return new UserNotFoundException("Неавторизованный доступ");
                });
    }
}
