package org.example.cloudstorage.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);//если пользователь не найден отправляется сообщение об ошибке
    }
}