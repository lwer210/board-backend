package com.example.board.comment.persistence.repository;

import com.example.board.comment.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
}
