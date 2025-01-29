package org.example.cloudstorage.controller;

import org.example.cloudstorage.dto.FileDTO;
import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.service.AuthorizationService;
import org.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController // класс контроллер
@RequestMapping("/cloud") // общий путь для всех методов класса
public class FileController {


    @Autowired // автоматически создается объект FileService
    private FileService fileService;


    @Autowired // автоматически создается объект AuthorizationService
    private AuthorizationService authService;



    // Обработка OPTIONS запроса для /cloud/login
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForLogin() {
        return ResponseEntity.ok().build(); // Возвращаем пустой ответ
    }


    @PostMapping("/login")// Обрабатывает POST-запросы по адресу /cloud/login
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
        String login = loginRequest.get("login"); // извлекается лог из запроса
        String password = loginRequest.get("password");// извлекается пароль из запроса
        String authToken = authService.authenticate(login, password); // аутентифицируется пользователь и создается токен
        return ResponseEntity.ok(authToken); // Возвращается токен как строка
    }


    @PostMapping("/logout")// Обрабатывает POST-запросы по адресу /cloud/logout
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);//удаляется токена из базы данных
        return ResponseEntity.ok().build();// Возвращает статус 200 OK без тела ответа
    }


    @PostMapping("/file")// Обрабатывает POST-запросы по адресу /cloud/file
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,//извлекается токен из заголовка
            @RequestParam("filename") String filename, //извлекается имя файла из параметров запроса
            @RequestParam("file") MultipartFile file) { // извлекает файл из параметров запроса

        fileService.uploadFile(authToken, filename, file);// загружается файл
        return ResponseEntity.ok().build();  // Возвращает статус 200 OK без тела ответа
    }


    @DeleteMapping("/file")// Обрабатывает DELETE-запросы по адресу /cloud/file
    public ResponseEntity<Void> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename); // удаляется файл
        return ResponseEntity.ok().build();//возвращается сообщение об удалении и статус 200 ок
    }


    @GetMapping("/file") // Обрабатывает GET-запросы по адресу /cloud/file
    public ResponseEntity<byte[]> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        byte[] fileData = fileService.downloadFile(authToken, filename);// загружается файл
        return ResponseEntity.ok(fileData);// Возвращает содержимое файла с HTTP статусом 200 OK
    }


    @GetMapping("/list") // Обрабатывает GET-запросы по адресу /cloud/list
    public ResponseEntity<List<FileDTO>> listFiles(
            @RequestHeader("auth-token") String authToken) { // Получает список файлов пользователя
        List<FileDTO> files = fileService.listFiles(authToken);//получение списка файлов в виде объектов FileDTO
        return ResponseEntity.ok(files);// Возвращается список файлов с HTTP статусом 200 OK
    }
}
