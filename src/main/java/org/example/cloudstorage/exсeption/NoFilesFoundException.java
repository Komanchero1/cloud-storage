package org.example.cloudstorage.exсeption;


//класс предназначен для обработки ошибок, связанных с отсутствием файлов в системе
public class NoFilesFoundException extends RuntimeException {

    //конструктор принимает в качестве параметра сообщение об ошибке
    public NoFilesFoundException(String message) {
        super(message);//передается сообщение в родительский класс
    }

    //конструктор в качестве параметра принимает сообщение об ошибке и причину вызывающую эту ошибку
    public NoFilesFoundException(String message, Throwable cause) {
        super(message, cause);//передается информация в родительский класс
    }
}

