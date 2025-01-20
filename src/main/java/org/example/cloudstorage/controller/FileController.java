package org.example.cloudstorage.controller;

import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.service.AuthorizationService;
import org.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cloud")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AuthorizationService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
        String login = loginRequest.get("login");
        String password = loginRequest.get("password");
        String authToken = authService.authenticate(login, password);
        return ResponseEntity.ok(authToken); // Возвращается токен как строка
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {
        fileService.uploadFile(authToken, filename, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        byte[] fileData = fileService.downloadFile(authToken, filename);
        return ResponseEntity.ok(fileData);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> listFiles(
            @RequestHeader("auth-token") String authToken) {
        List<FileEntity> files = fileService.listFiles(authToken);
        return ResponseEntity.ok(files);
    }
}
