package org.example.cloudstorage.exсeption;


//класс  для обработки ошибок, связанных с загрузкой файлов
public class FileUploadException extends RuntimeException {

    //конструктор в качестве параметра принимает сообщение об ошибке
    public FileUploadException(String message) {
        super(message);//передается сообщение в родительский класс
    }


    //конструктор принимающий в качестве параметра сообщение об ошибке и причину по которой вызвано это исключение
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);//информацию передаем в родительский класс
    }
}
