package org.example.cloudstorage.repository;

import org.example.cloudstorage.model.FileEntity;
import org.example.cloudstorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByFilenameAndUser(String filename, User user);
    List<FileEntity> findByUserId(Long userId);
}
