package org.example.cloudstorage.model;

import java.time.LocalDateTime;


public class ErrorResponse {

    private LocalDateTime timestamp;//для хранения времени и даты
    private int status;//для хранения статуса
    private String message;// для хранения текста сообщения
    private String error;//для хранения общего содержания ошибки
    private String path;// для хранения пути

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
