package org.example.cloudstorage.model;

import jakarta.persistence.*;

import java.util.List;


//класс сущность отображаемая в базе данных
@Entity
//имя таблицы в базе данных, которая будет соответствовать этой сущности
@Table(name = "users")
public class User {
    @Id //поле является уникальным идентификатором сущности
    // автоматическая генерация значения идентификатора
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //для хранения идентификатора
    private String login; //для хранения логина
    private String passwordHash;//для хранения хеша пароля
    private String authToken;//для хранения токена

    //один пользователь может иметь множество файлов
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FileEntity> files; // связь с файлами принадлежащими пользователю

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }
}
