package org.example.cloudstorage.exсeption;


//класс предназначен для обработки ошибок, связанных с неверными учетными данными при аутентификации пользователей
public class InvalidCredentialsException extends RuntimeException {

    //конструктор принимает в качестве параметра сообщение об ошибке
    public InvalidCredentialsException(String message) {
        super(message);//предается сообщение в родительский класс
    }

    //конструктор принимающий в качестве параметра сообщение об ошибке и причине вызвавшей это исключение
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause); // Передаем сообщение и причину в родительский класс
    }
}