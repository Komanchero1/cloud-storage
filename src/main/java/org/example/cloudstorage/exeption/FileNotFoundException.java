package org.example.cloudstorage.exeption;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);//сообщение, что файл не был найден
    }
}