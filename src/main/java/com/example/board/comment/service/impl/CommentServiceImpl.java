package com.example.board.comment.service.impl;

import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.persistence.entity.CommentEntity;
import com.example.board.comment.persistence.repository.CommentEntityRepository;
import com.example.board.comment.service.CommentService;
import com.example.board.common.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;

    @Override
    public CommentResponse add(Long articleSeq, CommentRequest request) {
        ArticleEntity articleEntity = articleEntityRepository.findById(articleSeq)
                .orElseThrow(ArticleNotFoundException::new);

        CommentEntity comment = CommentEntity.builder()
                .answer(request.getAnswer())
                .like(0L)
                .article(articleEntity)
                .build();

        CommentEntity save = commentEntityRepository.save(comment);

        return CommentResponse.builder()
                .commentId(save.getId())
                .answer(save.getAnswer())
                .like(save.getLike())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();
    }

    @Override
    public void list(Long articleSeq, Pageable pageable) {

    }

    @Override
    public void update(Long commentSeq, CommentRequest request) {

    }

    @Override
    public void delete(Long commentSeq) {

    }

    @Override
    public void countList(Long commentSeq) {

    }
}
