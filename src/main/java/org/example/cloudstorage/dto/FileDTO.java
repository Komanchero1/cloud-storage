package org.example.cloudstorage.dto;


//класс для упрощения передачи данных и уменьшения объема передаваемой информации
public class FileDTO {
    private Long id; //индентификатор файла
    private String filename;//имя файла
    private Long size;//размер файла

    // Конструкторы для создаия объекта FileDTO
    public FileDTO(Long id, String filename, Long size) {
        this.id = id;
        this.filename = filename;
        this.size = size;
    }

    //геттеры и сеттары
    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public Long getSize() {
        return size;
    }
}
