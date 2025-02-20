package com.example.board.comment.persistence.repository;

import com.example.board.comment.persistence.entity.CommentLikeEntity;
import com.example.board.comment.persistence.entity.id.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, CommentLikeId> {

    boolean existsById(CommentLikeId id);
}
