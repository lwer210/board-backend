package com.example.board.file.persistence.repository;

import com.example.board.file.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
}
