package com.example.board.article.service.impl;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.request.UpdateArticleRequest;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.controller.response.DeleteArticleResponse;
import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.article.service.ArticleService;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.ArticleNotFoundException;
import com.example.board.common.exception.UnauthorizedException;
import com.example.board.common.exception.UserNotFoundException;
import com.example.board.common.paging.Pagination;
import com.example.board.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public PagingResponse<ArticleResponse> list(Pageable pageable) {
        Page<ArticleEntity> page = articleEntityRepository.findAllByPublicYn(pageable, "Y");

        Pagination pagination = Pagination.builder()
                .size(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .number(page.getNumber())
                .build();

        List<ArticleEntity> list = page.getContent();

        List<ArticleResponse> result = list.stream().map(value -> {
            return ArticleResponse.builder()
                    .articleId(value.getId())
                    .title(value.getTitle())
                    .content(value.getContent())
                    .publicYn(value.getPublicYn())
                    .build();
        }).toList();

        return PagingResponse.<ArticleResponse>builder()
                .content(result)
                .pagination(pagination)
                .build();
    }

    @Override
    public PagingResponse<ArticleResponse> getUserArticleList(CustomUserDetails userDetails, Pageable pageable) {
        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        Page<ArticleEntity> page = articleEntityRepository.
                findAllByUserIdAndPublicYn(user.getId(), "Y", pageable);

        Pagination pagination = Pagination.builder()
                .size(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .number(page.getNumber())
                .build();

        List<ArticleResponse> list = page.getContent().stream()
                .map(value -> {
                    return ArticleResponse.builder()
                            .articleId(value.getId())
                            .title(value.getTitle())
                            .content(value.getContent())
                            .publicYn(value.getPublicYn())
                            .build();
                }).toList();

        return PagingResponse.<ArticleResponse>builder()
                .pagination(pagination)
                .content(list)
                .build();
    }

    @Override
    public ArticleResponse info(Long articleId) {
        ArticleEntity articleEntity = articleEntityRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        return ArticleResponse.builder()
                .articleId(articleEntity.getId())
                .title(articleEntity.getTitle())
                .content(articleEntity.getContent())
                .publicYn(articleEntity.getPublicYn())
                .build();
    }

    @Override
    public ArticleResponse add(CustomUserDetails userDetails, ArticleRequest request) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        ArticleEntity entity = ArticleEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .publicYn(request.getPublicYn())
                .user(user)
                .build();

        articleEntityRepository.save(entity);

        return ArticleResponse.builder()
                .articleId(entity.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .publicYn(request.getPublicYn())
                .build();
    }

    @Override
    public ArticleResponse update(CustomUserDetails userDetails, UpdateArticleRequest request) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        ArticleEntity article = articleEntityRepository.findById(request.getArticleId())
                .orElseThrow(ArticleNotFoundException::new);

        if(request.getContent() != null){
            article.setContent(request.getContent());
        }
        if(request.getTitle() != null){
            article.setTitle(request.getTitle());
        }
        if(request.getPublicYn() != null){
            article.setPublicYn(request.getPublicYn());
        }

        ArticleEntity save = articleEntityRepository.save(article);

        return ArticleResponse.builder()
                .articleId(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .publicYn(save.getPublicYn())
                .build();
    }

    @Override
    public DeleteArticleResponse delete(Long articleId) {
        ArticleEntity articleEntity = articleEntityRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        articleEntityRepository.delete(articleEntity);

        return DeleteArticleResponse.builder()
                .isDelete(true)
                .build();
    }
}
