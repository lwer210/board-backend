package com.example.board.comment.service.impl;

import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.persistence.entity.CommentEntity;
import com.example.board.comment.persistence.repository.CommentEntityRepository;
import com.example.board.comment.service.CommentService;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.ArticleNotFoundException;
import com.example.board.common.exception.UnauthorizedException;
import com.example.board.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public CommentResponse add(Long articleSeq, CommentRequest request, CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        ArticleEntity articleEntity = articleEntityRepository.findByIdAndPublicYn(articleSeq, "Y")
                .orElseThrow(ArticleNotFoundException::new);

        CommentEntity comment = CommentEntity.builder()
                .answer(request.getAnswer())
                .like(0L)
                .article(articleEntity)
                .userSeq(user.getId())
                .build();

        CommentEntity save = commentEntityRepository.save(comment);

        return CommentResponse.builder()
                .commentId(save.getId())
                .nickname(user.getNickname())
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
    public void update(Long commentSeq, CommentRequest request, CustomUserDetails customUserDetails) {

    }

    @Override
    public void delete(Long commentSeq, CustomUserDetails customUserDetails) {

    }

    @Override
    public void countAdd(Long commentSeq, CustomUserDetails customUserDetails) {

    }
}
