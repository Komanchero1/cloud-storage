package org.example.cloudstorage.exeption;


import org.example.cloudstorage.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice //класс обрабатывает исключения всего приложения
public class GlobalExceptionHandler {


    // логгер для записи сообщений об ошибках
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // Обработчик исключений для случая, когда пользователь не найден
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        logger.warn("Пользователь не найден: {}", ex.getMessage());// логгирует предупреждение
        // Возвращает ответ с ошибкой 404
        return createErrorResponse(HttpStatus.NOT_FOUND, "Пользователь не найден", ex, request);
    }


    // Обработчик исключений для неверных учетных данных
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        logger.warn("Неверный логин или пароль: {}", ex.getMessage());// логгирует предупреждение
        // Возвращает ответ с ошибкой 401
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль", ex, request);
    }


    // Обработчик исключений для случая, когда файл не найден
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException ex, WebRequest request) {
        logger.warn("Файл не найден: {}", ex.getMessage());// логгирует предупреждение
        // Возвращает ответ с ошибкой 404
        return createErrorResponse(HttpStatus.NOT_FOUND, "Файл не найден", ex, request);
    }


    // Обработчик для всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        logger.error("Ошибка сервера: {}", ex.getMessage(), ex);// логгирует предупреждение
        // Возвращает ответ с ошибкой 500
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сервера", ex, request);
    }


    // Метод для создания стандартного ответа об ошибке
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message, Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();//создается объект
        errorResponse.setTimestamp(LocalDateTime.now());//устанавливается временная метка
        errorResponse.setStatus(status.value());//статус HTTP
        errorResponse.setError(message);//сообщение об ошибке
        errorResponse.setMessage(ex.getMessage());// детальное сообщение об ошибке
        errorResponse.setPath(request.getDescription(false));//путь запроса
        return new ResponseEntity<>(errorResponse, status); // возвращает сформированный объект ErrorResponse
    }
}
