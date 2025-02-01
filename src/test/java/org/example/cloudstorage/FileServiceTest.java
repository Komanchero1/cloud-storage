package org.example.cloudstorage;

import org.example.cloudstorage.dto.FileDTO;
import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.FileRepository;
import org.example.cloudstorage.repository.UserRepository;
import org.example.cloudstorage.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock //  создается заглушка для UserRepository
    private UserRepository userRepository;

    @Mock //  создается заглушка для FileRepository
    private FileRepository fileRepository;

    @Mock //  создается заглушка для MultipartFile
    private MultipartFile file;

    @InjectMocks // внедряются моки в экземпляр FileService
    private FileService fileService;

    private User user;

    @BeforeEach // выполняется перед каждым тестом
    public void setUp() {
        MockitoAnnotations.openMocks(this); // инициализация моков перед выполнением тестов
        user = new User(); // создается новый пользователь
        user.setId(1L); // устанавливается идентификатор
        user.setLogin("comanch@mail.ru"); // устанавливается логин
    }

    // Тест для успешной проверки загрузки файла
    @Test
    public void testUploadFileSuccess() throws Exception {
        String filename = "testfile.txt"; // присваивается значение переменной
        //настройка поведения userRepository при вызове findByAuthToken с аргументом "validToken"  должен вернуться Optional содержащий user
        when(userRepository.findByAuthToken("validToken")).thenReturn(Optional.of(user));
        //настройка поведения file при вызове getOriginalFilename он должен вернуть filename("testfile.txt")
        when(file.getOriginalFilename()).thenReturn(filename);
        //настройка поведения file при вызове getSize должен вернуть 123
        when(file.getSize()).thenReturn(123L);
        //настройка поведения userRepository при вызове findByFilenameAndUser с аргументами (filename, user) должен вернуть Optional.empty()
        // что означает что файл не существует
        when(fileRepository.findByFilenameAndUser(filename, user)).thenReturn(Optional.empty()); // файл не существует

        // Вызов метода uploadFile, загрузка файла
        fileService.uploadFile("validToken", filename, file);

        // Проверка, что метод save был вызван на fileRepository
        verify(fileRepository).save(any(FileEntity.class));
    }


    // Тест для проверки успешного удаления файла
    @Test
    public void testDeleteFileSuccess() throws FileNotFoundException {
        String filename = "testfile.txt"; //задается имя файла
        String filePath = "path/to/" + filename; // задается путь к файлу
        FileEntity fileEntity = new FileEntity(); // создание объекта FileEntity который представляет файл
        fileEntity.setFilename(filename); // установка имени файла в объекте fileEntity
        fileEntity.setFilePath(filePath); // установка пути к файлу в объекте fileEntity
        fileEntity.setUser(user); // установка пользователя которому принадлежит файл

        //настройка userRepository чтобы он возвращал пользователя если токен действителен
        when(userRepository.findByAuthToken("validToken")).thenReturn(Optional.of(user));
        // настройка поведения fileRepository, чтобы он возвращал fileEntity при поиске файла по имени и пользователю
        when(fileRepository.findByFilenameAndUser(filename, user)).thenReturn(Optional.of(fileEntity)); // файл найден


        new File(filePath).getParentFile().mkdirs(); // создание директорий, если они не существуют
        try {
            Files.createFile(Paths.get(filePath)); // создание файла
        } catch (IOException e) { // обработка исключения, если создание файла не удалось
            e.printStackTrace();
        }

        // Вызов метода deleteFile
        fileService.deleteFile("validToken", filename);

        // Проверка, что метод delete был вызван на fileRepository
        verify(fileRepository).delete(fileEntity);

        // Проверка, что файл был удален с диска
        assertFalse("Файл не должен существовать после удаления", new File(filePath).exists());
    }



    // Тест для проверки успешного скачивания файла
    @Test
    public void testDownloadFileSuccess() throws IOException {
        String filename = "testfile.txt"; //задается имя файла
        String filePath = "path/to/" + filename; // задается путь к файлу
        FileEntity fileEntity = new FileEntity(); // создается объект FileEntity который будет представлять файл
        fileEntity.setFilename(filename); // установка имени файла в объекте fileEntity
        fileEntity.setFilePath(filePath); // установка пути к файлу в объекте fileEntity
        fileEntity.setUser(user); // установка пользователя которому принадлежит файл

        //настройка userRepository, чтобы он возвращал пользователя при передаче действительного токена
        when(userRepository.findByAuthToken("validToken")).thenReturn(Optional.of(user));
        //настройка fileRepository, чтобы он возвращал fileEntity при поиске файла по имени и пользователю
        when(fileRepository.findByFilenameAndUser(filename, user)).thenReturn(Optional.of(fileEntity));

        // создание директории если она не существует
        new File(filePath).getParentFile().mkdirs();
        Files.createFile(Paths.get(filePath)); // создание файла
        Files.write(Paths.get(filePath), "file content".getBytes()); // запись содержимого в файл

        // Вызов метода downloadFile для загрузки файла
        byte[] content = fileService.downloadFile("validToken", filename);

        // Проверка, что содержимое файла соответствует ожидаемому
        assertEquals("file content", new String(content));

        // Удаление созданного файла после теста (опционально)
        new File(filePath).delete();
    }


    // Тест для проверки успешного получения списка файлов
    @Test
    public void testListFilesSuccess() {
        //настройка userRepository, вызов метода findByAuthToken("validToken") должен вернуть Optional с объектом user
        when(userRepository.findByAuthToken("validToken")).thenReturn(Optional.of(user));
        //настройка fileRepository, вызов метода findByUserId с идентификатором пользователя вернет список содержащий FileEntity
        when(fileRepository.findByUserId(user.getId())).thenReturn(List.of(new FileEntity()));

        // Вызов метода listFiles, возвращает список файлов
        List<FileDTO> files = fileService.listFiles("validToken");

        // Проверка, что список файлов не пустой
        assertFalse(files.isEmpty());
    }
}

