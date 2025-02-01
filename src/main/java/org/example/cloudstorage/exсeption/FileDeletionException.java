package org.example.cloudstorage.exсeption;


//класс для обработки ошибок, связанных с удалением файлов
public class FileDeletionException extends RuntimeException {

    //конструктор принимающий в качестве параметра сообщение об ошибке
    public FileDeletionException(String message) {
        super(message);//передается сообщение в родительский класс
    }

    //конструктор принимающий в качестве параметра сообщение об ошибке и причину ошибки
    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);// передаем параметры в родительский класс
    }
}
