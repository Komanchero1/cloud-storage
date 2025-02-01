package org.example.cloudstorage.exсeption;


//класс предназначен для обработки ошибок, связанных с несанкционированным доступом к ресурсам или операциям в приложении
public class UnauthorizedAccessException extends RuntimeException {

    //конструктор принимающий в качестве параметра сообщение об ошибке
    public UnauthorizedAccessException(String message) {
        super(message);// передается сообщение в родительский класс
    }

    //конструктор принимает в качестве параметра сообщение об ошибке и причину вызвавшую эту ошибку
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);//передается информация в родительский класс
    }
}
