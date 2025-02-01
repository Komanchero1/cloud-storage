package org.example.cloudstorage.exсeption;


//класс для обработки ошибок, связанных со скачиванием файлов
public class FileDownloadException extends RuntimeException {

    //конструктор принимающий в качестве параметра сообщение об ошибке
    public FileDownloadException(String message) {
        super(message);//передается сообщение в родительский класс
    }

    //конструктор принимающий сообщение об ошибке и исключение из за которого вызвано текущее исключение
    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);//передается информация в родительский класс
    }
}
