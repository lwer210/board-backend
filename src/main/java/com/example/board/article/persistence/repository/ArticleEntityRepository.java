package com.example.board.article.persistence.repository;

import com.example.board.article.persistence.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {
}
