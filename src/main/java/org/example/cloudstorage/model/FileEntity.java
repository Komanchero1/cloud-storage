package org.example.cloudstorage.model;

import jakarta.persistence.*;

@Entity//класс сущность отображаемая в базе данных
public class FileEntity {


    @Id //поле является уникальным идентификатором сущности
    // Автоматическая генерация значения идентификатора
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//для хранения идентификатора


    private String filename; // имя файла
    private Long size; // размер файла
    private String filePath; // путь к файлу

    @ManyToOne // Указывает, что у файла может быть связь с одним пользователем
    //имя колонки в базе данных, которая будет хранить идентификатор пользователя
    // и что она не может быть пустой
    @JoinColumn(name = "userId", nullable = false)
    private User user; // связь с пользователем которому принадлежит файл

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
