package com.example.board.comment.service.impl;

import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.controller.response.DeleteCommentResponse;
import com.example.board.comment.persistence.entity.CommentEntity;
import com.example.board.comment.persistence.repository.CommentEntityRepository;
import com.example.board.comment.service.CommentService;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.*;
import com.example.board.common.paging.Pagination;
import com.example.board.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PagingResponse<CommentResponse> list(Long articleSeq, Pageable pageable, CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            throw new UnauthorizedException();
        }

        Page<CommentEntity> page = commentEntityRepository.findAllByArticle_Id(articleSeq, pageable);

        List<CommentResponse> content = page.getContent().stream()
                .map(value -> {
                    UserEntity user = userEntityRepository.findById(value.getUserSeq())
                            .orElseThrow(UserNotFoundException::new);

                    return CommentResponse.builder()
                            .commentId(value.getId())
                            .nickname(user.getNickname())
                            .answer(value.getAnswer())
                            .like(value.getLike())
                            .createdAt(value.getCreatedAt())
                            .updatedAt(value.getUpdatedAt())
                            .build();
                }).toList();

        Pagination pagination = Pagination.builder()
                .size(page.getSize())
                .number(page.getNumber())
                .numberOfElement(page.getNumberOfElements())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return PagingResponse.<CommentResponse>builder()
                .content(content)
                .pagination(pagination)
                .build();
    }

    @Override
    public CommentResponse update(Long commentSeq, CommentRequest request, CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        CommentEntity comment = commentEntityRepository.findById(commentSeq)
                .orElseThrow(CommentNotFoundException::new);

        if(!comment.getUserSeq().equals(user.getId())){
            throw new UserSeqNotMatchesException("다른 사용자가 작성한 댓글입니다.");
        }

        if(request.getAnswer() != null){
            comment.setAnswer(request.getAnswer());
        }

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
    public DeleteCommentResponse delete(Long commentSeq, CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        CommentEntity comment = commentEntityRepository.findById(commentSeq)
                .orElseThrow(CommentNotFoundException::new);

        if(!comment.getUserSeq().equals(user.getId())){
            throw new UserSeqNotMatchesException("다른 사용자가 작성한 댓글입니다.");
        }

        commentEntityRepository.delete(comment);

        return DeleteCommentResponse.builder()
                .isDelete(true)
                .build();
    }

    @Override
    public void countAdd(Long commentSeq, CustomUserDetails customUserDetails) {

    }
}
