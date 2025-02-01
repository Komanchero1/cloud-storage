package org.example.cloudstorage.exсeption;


import org.example.cloudstorage.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

@ControllerAdvice // класс обрабатывает исключения всего приложения
public class GlobalExceptionHandler {

    // логгер для записи сообщений об ошибках
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Обработчик исключений для случая, когда пользователь не найден
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        logger.warn("Пользователь не найден: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.NOT_FOUND, "Пользователь не найден", ex, request);
    }

    // Обработчик исключений для неверных учетных данных
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        logger.warn("Неверный логин или пароль: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль", ex, request);
    }

    // Обработчик исключений для случая, когда файл не найден
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException ex, WebRequest request) {
        logger.warn("Файл не найден: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.NOT_FOUND, "Файл не найден", ex, request);
    }

    // Обработчик исключений для случая, когда файл уже существует
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFileAlreadyExists(FileAlreadyExistsException ex, WebRequest request) {
        logger.warn("Файл уже существует: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.CONFLICT, "Файл уже существует", ex, request); // 409 Conflict
    }

    // Обработчик исключений для случая, когда произошла ошибка удаления файла
    @ExceptionHandler(FileDeletionException.class)
    public ResponseEntity<ErrorResponse> handleFileDeletionException(FileDeletionException ex, WebRequest request) {
        logger.warn("Ошибка при удалении файла: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при удалении файла", ex, request); // 500 Internal Server Error
    }

    // Обработчик исключений для случая, когда произошла ошибка загрузки файла
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException ex, WebRequest request) {
        logger.warn("Ошибка при загрузке файла: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при загрузке файла", ex, request); // 500 Internal Server Error
    }

    // Обработчик исключений для случая, когда произошла ошибка скачивания файла
    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<ErrorResponse> handleFileDownloadException(FileDownloadException ex, WebRequest request) {
        logger.warn("Ошибка при скачивании файла: {}", ex.getMessage()); // логгирует предупреждение
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при скачивании файла", ex, request); // 500 Internal Server Error
    }

    // Обработчик исключений для несанкционированного доступа
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex, WebRequest request) {
        logger.warn("Неавторизованный доступ: {}", ex.getMessage()); // логгируется предупреждение
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Неавторизованный доступ", ex, request); // 401 Unauthorized
    }

    //обработчик исключений если список пуст
    @ExceptionHandler(NoFilesFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoFilesFound(NoFilesFoundException ex, WebRequest request) {
        logger.warn("Ошибка: {}", ex.getMessage());// логгируется предупреждение
        return createErrorResponse(HttpStatus.NOT_FOUND, "Нет файлов для просмотра", ex, request); // 404 Not Found
    }


    // Метод для создания стандартного ответа об ошибке
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message, Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(); // создается объект
        errorResponse.setTimestamp(LocalDateTime.now()); // устанавливается временная метка
        errorResponse.setStatus(status.value()); // статус HTTP
        errorResponse.setError(message); // сообщение об ошибке
        errorResponse.setMessage(ex.getMessage()); // детальное сообщение об ошибке
        errorResponse.setPath(request.getDescription(false)); // путь запроса
        return new ResponseEntity<>(errorResponse, status); // возвращает сформированный объект ErrorResponse
    }

    // Обработчик для всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        logger.error("Ошибка сервера: {}", ex.getMessage(), ex); // логгирует предупреждение
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сервера", ex, request); //500 Internal Server Error
    }

}
