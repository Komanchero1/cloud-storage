package org.example.cloudstorage.exсeption;


//класс предназначен для обработки ошибок, связанных с отсутствием пользователя в системе
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);//сообщение типа "пользователь не найден " передается в родительский класс
    }

    //конструктор принимающий в качестве параметров сообщение об ошибке и причину ошибки
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause); // Передаем сообщение и причину в родительский класс
    }
}