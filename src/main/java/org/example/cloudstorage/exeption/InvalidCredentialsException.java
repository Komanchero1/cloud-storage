package org.example.cloudstorage.exeption;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);//если неправильный логин или пароль отправляется сообщение об ошибке
    }
}