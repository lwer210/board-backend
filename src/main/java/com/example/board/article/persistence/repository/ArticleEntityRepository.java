package com.example.board.article.persistence.repository;

import com.example.board.article.persistence.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {
    Page<ArticleEntity> findAllByPublicYn(Pageable pageable, String publicYn);

    Page<ArticleEntity> findAllByUserId(Long userId, Pageable pageable);

    Optional<ArticleEntity> findByIdAndPublicYn(Long id, String publicYn);
}
